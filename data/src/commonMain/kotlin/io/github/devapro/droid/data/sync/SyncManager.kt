package io.github.devapro.droid.data.sync

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.data.sync.model.AuthResponse
import io.github.devapro.droid.data.sync.model.ChangesResponse
import io.github.devapro.droid.data.sync.model.PushItem
import io.github.devapro.droid.data.vault.CryptoMapper
import io.github.devapro.droid.data.vault.VaultDescriptor
import io.github.devapro.droid.data.vault.VaultFileRepository
import io.github.devapro.droid.data.vault.VaultItemModel
import io.github.devapro.droid.data.vault.VaultModel
import io.github.devapro.droid.data.vault.VaultRegistryRepository
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private const val PULL_PAGE_SIZE = 500

data class SyncSummary(
    val pushed: Int = 0,
    val pulled: Int = 0,
    val deleted: Int = 0
) {
    val isEmpty: Boolean get() = pushed == 0 && pulled == 0 && deleted == 0
}

/**
 * Orchestrates end-to-end-encrypted, incremental synchronization between the
 * local vault and the sync server.
 *
 * - Items are encrypted per-item with the master password before upload, so the
 *   server only ever stores opaque blobs.
 * - Only locally changed items (the vault's dirty set) and pending deletions
 *   (tombstones) are pushed.
 * - Only server changes newer than [VaultRuntimeRepository.getLastSyncSeq] are
 *   pulled. Conflicts are resolved last-write-wins by `updatedAt`.
 */
class SyncManager(
    private val syncApi: SyncApi,
    private val runtimeRepository: VaultRuntimeRepository,
    private val registryRepository: VaultRegistryRepository,
    private val vaultFileRepository: VaultFileRepository,
    private val cryptoMapper: CryptoMapper,
    private val syncStateStore: SyncStateStore,
    private val json: Json
) {

    suspend fun register(url: String, username: String, password: String): AppResult<Unit> =
        authenticate(url) { syncApi.register(url, username, password) }

    suspend fun login(url: String, username: String, password: String): AppResult<Unit> =
        authenticate(url) { syncApi.login(url, username, password) }

    suspend fun logout() {
        syncStateStore.clearAccount()
    }

    /**
     * Two-way sync of every unlocked vault: each is pulled, pushed, then re-pulled to
     * reconcile against its own server namespace. The originally active vault is restored
     * afterwards. Fails only if no vault could be synced; a per-vault error is kept so a
     * partial success is still reported.
     */
    suspend fun syncNow(): AppResult<SyncSummary> = withCredentials { url, token ->
        val loaded = runtimeRepository.listLoaded()
        if (loaded.isEmpty()) return@withCredentials AppResult.Failure(Exception("Vault is locked"))

        val originalActiveId = runtimeRepository.getActiveVaultId()
        var total = SyncSummary()
        var lastError: Exception? = null

        for (descriptor in loaded) {
            // The vault may have been unloaded since `loaded` was captured; skip it
            // rather than risk syncing/persisting the previously active vault.
            if (!runtimeRepository.setActiveVault(descriptor.id)) continue
            when (val result = syncActiveVault(url, token)) {
                is AppResult.Success -> total = total.merge(result.value)
                is AppResult.Failure -> lastError = result.error
            }
        }

        originalActiveId?.let { if (runtimeRepository.isLoaded(it)) runtimeRepository.setActiveVault(it) }

        val error = lastError
        if (error != null && total.isEmpty) {
            AppResult.Failure(error)
        } else {
            recordSync(total)
            AppResult.Success(total)
        }
    }

    /** Two-way sync of the currently active vault against its server namespace. */
    private suspend fun syncActiveVault(url: String, token: String): AppResult<SyncSummary> {
        var summary = SyncSummary()

        when (val pull = doPull(url, token)) {
            is AppResult.Success -> summary = summary.merge(pull.value.summary)
            is AppResult.Failure -> {
                // An earlier page may have already been applied in-memory; persist it.
                persistActive()
                return AppResult.Failure(pull.error)
            }
        }
        when (val push = doPush(url, token)) {
            is AppResult.Success -> {
                summary = summary.merge(push.value.summary)
                // Re-pull whenever anything was pushed OR the server rejected some of
                // our items (it has a newer version we must pull down to reconcile).
                if (push.value.attempted) {
                    when (val pull2 = doPull(url, token)) {
                        is AppResult.Success -> summary = summary.merge(pull2.value.summary)
                        is AppResult.Failure -> {
                            persistActive()
                            return AppResult.Failure(pull2.error)
                        }
                    }
                }
            }
            is AppResult.Failure -> {
                // Remote changes from the first pull were already applied in memory;
                // persist them so they survive even though the push failed.
                persistActive()
                return AppResult.Failure(push.error)
            }
        }

        persistActive()
        return AppResult.Success(summary)
    }

    /** Uploads local changes only. */
    suspend fun pushToServer(): AppResult<SyncSummary> = withCredentials { url, token ->
        if (!isUnlocked()) return@withCredentials AppResult.Failure(Exception("Vault is locked"))
        when (val push = doPush(url, token)) {
            is AppResult.Success -> {
                persistAndRecord(push.value.summary)
                AppResult.Success(push.value.summary)
            }
            is AppResult.Failure -> AppResult.Failure(push.error)
        }
    }

    /** Downloads remote changes only. */
    suspend fun pullFromServer(): AppResult<SyncSummary> = withCredentials { url, token ->
        if (!isUnlocked()) return@withCredentials AppResult.Failure(Exception("Vault is locked"))
        when (val pull = doPull(url, token)) {
            is AppResult.Success -> {
                persistAndRecord(pull.value.summary)
                AppResult.Success(pull.value.summary)
            }
            is AppResult.Failure -> AppResult.Failure(pull.error)
        }
    }

    /**
     * First-time setup on a new device: discovers every vault on the account and
     * restores each one that [masterPassword] can decrypt as an independent local
     * vault. A vault file is only written once the pull has proven the password can
     * decrypt that vault's payload, so a wrong master password never leaves an empty
     * vault behind that would block a retry. The first restored vault becomes active.
     */
    suspend fun restoreVaultFromServer(masterPassword: String): AppResult<SyncSummary> =
        withCredentials { url, token ->
            if (masterPassword.isBlank()) {
                return@withCredentials AppResult.Failure(Exception("Master password cannot be empty"))
            }

            val remoteVaults = when (val r = syncApi.listVaults(url, token)) {
                is AppResult.Success -> r.value.vaults
                is AppResult.Failure -> return@withCredentials AppResult.Failure(r.error)
            }

            // Nothing on the server yet: create a single fresh local vault so the
            // account is usable and future pushes have a home.
            if (remoteVaults.isEmpty()) {
                val descriptor = newDescriptor(name = "My Vault")
                runtimeRepository.loadVault(
                    descriptor = descriptor,
                    vault = VaultModel(password = masterPassword, items = emptyList(), name = descriptor.name)
                )
                runtimeRepository.setActiveVault(descriptor.id)
                registryRepository.addVault(descriptor)
                registryRepository.setActiveVaultId(descriptor.id)
                persistAndRecord(SyncSummary())
                return@withCredentials AppResult.Success(SyncSummary())
            }

            var total = SyncSummary()
            var firstRestoredId: String? = null

            for ((index, remote) in remoteVaults.withIndex()) {
                val name = if (remoteVaults.size == 1) "My Vault" else "Vault ${index + 1}"
                val descriptor = newDescriptor(id = remote.vaultId, name = name)
                // In-memory only — nothing is persisted until the pull succeeds.
                runtimeRepository.loadVault(
                    descriptor = descriptor,
                    vault = VaultModel(password = masterPassword, items = emptyList(), name = name)
                )
                runtimeRepository.setActiveVault(descriptor.id)

                when (val pull = doPull(url, token)) {
                    is AppResult.Failure -> {
                        runtimeRepository.unloadVault(descriptor.id)
                        return@withCredentials AppResult.Failure(pull.error)
                    }
                    is AppResult.Success -> {
                        val result = pull.value
                        if (result.encryptedSeen > 0 && result.decrypted == 0) {
                            // Wrong master password for this vault — skip it.
                            runtimeRepository.unloadVault(descriptor.id)
                        } else {
                            registryRepository.addVault(descriptor)
                            vaultFileRepository.saveVault(
                                descriptor = descriptor,
                                vaultModel = runtimeRepository.getVault()
                            )
                            total = total.merge(result.summary)
                            if (firstRestoredId == null) firstRestoredId = descriptor.id
                        }
                    }
                }
            }

            val activeId = firstRestoredId
            if (activeId == null) {
                // Every vault held encrypted items none of which decrypted.
                return@withCredentials AppResult.Failure(
                    Exception("Incorrect master password. Use the master password from your other device.")
                )
            }
            runtimeRepository.setActiveVault(activeId)
            registryRepository.setActiveVaultId(activeId)
            persistAndRecord(total)
            AppResult.Success(total)
        }

    // --- Internals ---------------------------------------------------------

    private suspend fun authenticate(
        url: String,
        request: suspend () -> AppResult<AuthResponse>
    ): AppResult<Unit> {
        return when (val result = request()) {
            is AppResult.Success -> {
                syncStateStore.setServerUrl(url)
                syncStateStore.setAccount(result.value.username, result.value.token)
                AppResult.Success(Unit)
            }
            is AppResult.Failure -> AppResult.Failure(result.error)
        }
    }

    private suspend fun doPull(url: String, token: String): AppResult<PullResult> {
        val vaultId = runtimeRepository.getActiveDescriptor().id
        var pulled = 0
        var deleted = 0
        var encryptedSeen = 0
        var decrypted = 0
        var since = runtimeRepository.getLastSyncSeq()

        while (true) {
            val response: ChangesResponse =
                when (val r = syncApi.getChanges(url, token, vaultId, since, PULL_PAGE_SIZE)) {
                    is AppResult.Success -> r.value
                    is AppResult.Failure -> return AppResult.Failure(r.error)
                }

            // Snapshot the vault once per page for O(1) lookups instead of scanning
            // the whole item list for every incoming change.
            val vault = runtimeRepository.getVault()
            val itemsById = vault.items.associateBy { it.id }
            val tombstonesById = vault.tombstones.associateBy { it.id }

            for (change in response.items) {
                val localItem = itemsById[change.id]
                // Consider a pending local deletion (tombstone) when resolving the
                // conflict, otherwise a remote upsert would resurrect a deleted item.
                val localUpdatedAt = when {
                    localItem != null -> effectiveUpdatedAt(localItem)
                    else -> tombstonesById[change.id]?.deletedAt ?: 0L
                }
                if (change.deleted) {
                    if (localItem != null && change.updatedAt >= localUpdatedAt) {
                        runtimeRepository.applyRemoteDelete(change.id)
                        deleted++
                    }
                } else if (change.updatedAt > localUpdatedAt) {
                    val payload = change.payload ?: continue
                    encryptedSeen++
                    val item = decryptItem(payload) ?: continue
                    decrypted++
                    runtimeRepository.applyRemoteUpsert(item)
                    pulled++
                }
            }

            if (response.items.size < PULL_PAGE_SIZE) {
                runtimeRepository.setLastSyncSeq(
                    maxOf(runtimeRepository.getLastSyncSeq(), response.latestSeq)
                )
                break
            } else {
                since = response.items.last().seq
                runtimeRepository.setLastSyncSeq(maxOf(runtimeRepository.getLastSyncSeq(), since))
            }
        }
        return AppResult.Success(
            PullResult(
                summary = SyncSummary(pulled = pulled, deleted = deleted),
                encryptedSeen = encryptedSeen,
                decrypted = decrypted
            )
        )
    }

    private suspend fun doPush(url: String, token: String): AppResult<PushOutcome> {
        val vaultId = runtimeRepository.getActiveDescriptor().id
        val vault = runtimeRepository.getVault()
        val dirtyIds = (vault.dirtyItemIds + vault.tombstones.map { it.id }).distinct()
        if (dirtyIds.isEmpty()) {
            return AppResult.Success(PushOutcome(summary = SyncSummary(), attempted = false))
        }

        val pushItems = dirtyIds.mapNotNull { id ->
            val item = vault.items.firstOrNull { it.id == id }
            if (item != null) {
                val payload = encryptItem(item) ?: return@mapNotNull null
                PushItem(
                    id = id,
                    updatedAt = effectiveUpdatedAt(item),
                    deleted = false,
                    payload = payload
                )
            } else {
                val tombstone = vault.tombstones.firstOrNull { it.id == id }
                PushItem(
                    id = id,
                    updatedAt = tombstone?.deletedAt ?: 0L,
                    deleted = true,
                    payload = null
                )
            }
        }

        if (pushItems.isEmpty()) {
            return AppResult.Success(PushOutcome(summary = SyncSummary(), attempted = false))
        }

        return when (val r = syncApi.push(url, token, vaultId, pushItems)) {
            is AppResult.Success -> {
                // Every id in the response is now authoritative on the server (either it
                // accepted ours, or it kept a newer version we will fetch on the re-pull),
                // so it is safe to clear the local dirty/tombstone markers for all of them.
                val pushedIds = r.value.results.map { it.id }
                runtimeRepository.clearDirty(pushedIds)
                runtimeRepository.clearTombstones(pushedIds)
                AppResult.Success(
                    PushOutcome(
                        summary = SyncSummary(pushed = r.value.results.count { it.stored }),
                        attempted = true
                    )
                )
            }
            is AppResult.Failure -> AppResult.Failure(r.error)
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    private suspend fun encryptItem(item: VaultItemModel): String? = try {
        val masterPassword = runtimeRepository.getVault().password
        val raw = json.encodeToString(item)
        val cipher = cryptoMapper.encode(masterPassword, raw)
        Base64.encode(cipher)
    } catch (e: Exception) {
        null
    }

    @OptIn(ExperimentalEncodingApi::class)
    private suspend fun decryptItem(payload: String): VaultItemModel? = try {
        val masterPassword = runtimeRepository.getVault().password
        val cipher = Base64.decode(payload)
        val raw = cryptoMapper.decode(masterPassword, cipher)
        json.decodeFromString<VaultItemModel>(raw)
    } catch (e: Exception) {
        null
    }

    /**
     * The timestamp used for last-write-wins. Falls back to [VaultItemModel.createdAt]
     * for legacy items saved before `updatedAt` existed, so the same item resolves
     * identically whether it is being pushed or pulled.
     */
    private fun effectiveUpdatedAt(item: VaultItemModel): Long =
        item.updatedAt ?: item.createdAt ?: 0L

    private fun isUnlocked(): Boolean =
        runtimeRepository.getActiveVaultId() != null && runtimeRepository.getVault().password.isNotEmpty()

    @OptIn(ExperimentalTime::class)
    private fun newDescriptor(id: String = VaultDescriptor.newId(), name: String): VaultDescriptor {
        val now = Clock.System.now().toEpochMilliseconds()
        return VaultDescriptor(
            id = id,
            name = name,
            fileName = VaultDescriptor.newFileName(id),
            createdAt = now,
            updatedAt = now
        )
    }

    private suspend fun persistAndRecord(summary: SyncSummary) {
        persistActive()
        recordSync(summary)
    }

    /** Persists the active vault's current in-memory state to its encrypted file. */
    private suspend fun persistActive() {
        vaultFileRepository.saveVault(
            descriptor = runtimeRepository.getActiveDescriptor(),
            vaultModel = runtimeRepository.getVault()
        )
    }

    @OptIn(ExperimentalTime::class)
    private suspend fun recordSync(summary: SyncSummary) {
        val now = Clock.System.now().toEpochMilliseconds()
        val status = if (summary.isEmpty) {
            "Up to date"
        } else {
            "Synced: ${summary.pushed} up, ${summary.pulled} down, ${summary.deleted} removed"
        }
        syncStateStore.setLastSync(now, status)
    }

    private suspend fun withCredentials(
        block: suspend (url: String, token: String) -> AppResult<SyncSummary>
    ): AppResult<SyncSummary> {
        val url = syncStateStore.getServerUrl()
        val token = syncStateStore.getToken()
        if (url.isEmpty() || token.isEmpty()) {
            return AppResult.Failure(Exception("Not logged in to a sync server"))
        }
        return block(url, token)
    }

    private fun SyncSummary.merge(other: SyncSummary) = SyncSummary(
        pushed = pushed + other.pushed,
        pulled = pulled + other.pulled,
        deleted = deleted + other.deleted
    )

    private data class PullResult(
        val summary: SyncSummary,
        /** Number of non-deleted remote changes that carried an encrypted payload. */
        val encryptedSeen: Int,
        /** How many of [encryptedSeen] were successfully decrypted with the master password. */
        val decrypted: Int
    )

    private data class PushOutcome(
        val summary: SyncSummary,
        /** True when items were actually sent to the server (and a re-pull is warranted). */
        val attempted: Boolean
    )
}
