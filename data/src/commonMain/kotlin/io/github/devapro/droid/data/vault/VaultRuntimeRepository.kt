package io.github.devapro.droid.data.vault

import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * In-memory store of the unlocked vaults. Mutated from two concurrent contexts — the
 * MVI reducers (their own dispatcher) and the background sync (a separate dispatcher) —
 * so every operation is guarded by a reentrant lock to keep the loaded-map and active
 * pointer consistent. The lock is reentrant, so methods may call each other freely.
 */
class VaultRuntimeRepository {

    private data class Entry(val descriptor: VaultDescriptor, var vault: VaultModel)

    private val lock = SynchronizedObject()
    private val loaded = mutableMapOf<String, Entry>()
    private var activeVaultId: String? = null

    /**
     * When > 0, change notifications are coalesced into a single emission released by
     * [endNotificationBatch]. Sync brackets its whole run with this so the UI doesn't
     * flicker as the active vault is switched per-vault during the sweep.
     */
    private var notificationBatchDepth = 0
    private var pendingNotification = false

    private val _activeVaultChanges = MutableSharedFlow<Unit>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val activeVaultChanges: SharedFlow<Unit> = _activeVaultChanges.asSharedFlow()

    fun loadVault(descriptor: VaultDescriptor, vault: VaultModel) = synchronized(lock) {
        loaded[descriptor.id] = Entry(descriptor, vault)
        if (activeVaultId == null) activeVaultId = descriptor.id
        notifyChange()
    }

    fun setActiveVault(id: String): Boolean = synchronized(lock) {
        if (!loaded.containsKey(id)) return@synchronized false
        activeVaultId = id
        notifyChange()
        true
    }

    fun getActiveVaultId(): String? = synchronized(lock) { activeVaultId }

    fun isLoaded(id: String): Boolean = synchronized(lock) { loaded.containsKey(id) }

    fun unloadVault(id: String) = synchronized(lock) {
        loaded.remove(id)
        if (activeVaultId == id) {
            activeVaultId = loaded.keys.firstOrNull()
            notifyChange()
        }
    }

    fun unloadAll() = synchronized(lock) {
        loaded.clear()
        activeVaultId = null
        notifyChange()
    }

    fun getVault(): VaultModel = getActiveVault()

    fun getActiveVault(): VaultModel = synchronized(lock) { requireActiveEntry().vault }

    fun getActiveVaultOrNull(): VaultModel? = synchronized(lock) {
        val id = activeVaultId ?: return@synchronized null
        loaded[id]?.vault
    }

    fun getActiveDescriptor(): VaultDescriptor = synchronized(lock) { requireActiveEntry().descriptor }

    fun getVault(id: String): VaultModel? = synchronized(lock) { loaded[id]?.vault }

    fun getDescriptor(id: String): VaultDescriptor? = synchronized(lock) { loaded[id]?.descriptor }

    fun listLoaded(): List<VaultDescriptor> = synchronized(lock) { loaded.values.map { it.descriptor } }

    fun replaceActiveVault(vault: VaultModel) = synchronized(lock) {
        val entry = requireActiveEntry()
        entry.vault = vault
        notifyChange()
    }

    fun replaceActiveDescriptor(descriptor: VaultDescriptor) = synchronized(lock) {
        val entry = requireActiveEntry()
        val oldId = entry.descriptor.id
        if (oldId != descriptor.id) {
            loaded.remove(oldId)
            activeVaultId = descriptor.id
        }
        loaded[descriptor.id] = entry.copy(descriptor = descriptor)
    }

    fun addOrUpdateVault(item: VaultItemModel) = synchronized(lock) {
        val current = getActiveVault()
        val newItems = if (current.items.any { it.id == item.id }) {
            current.items.map { if (it.id == item.id) item else it }
        } else {
            current.items + item
        }
        replaceActiveVault(
            current.copy(
                items = newItems,
                updatedAt = nowMillis(),
                // Mark as locally changed and resurrect any pending tombstone.
                dirtyItemIds = (current.dirtyItemIds + item.id).distinct(),
                tombstones = current.tombstones.filterNot { it.id == item.id }
            )
        )
    }

    /**
     * Updates an existing item in place WITHOUT marking it dirty. For local-only
     * metadata changes (e.g. bumping `lastUsedAt` when an item is merely viewed)
     * that should not trigger a sync upload.
     */
    fun updateItemMetadata(item: VaultItemModel) = synchronized(lock) {
        val current = getActiveVault()
        if (current.items.none { it.id == item.id }) return@synchronized
        replaceActiveVault(
            current.copy(
                items = current.items.map { if (it.id == item.id) item else it }
            )
        )
    }

    fun deleteVaultById(itemId: String) = synchronized(lock) {
        val current = getActiveVault()
        val newItems = current.items.filter { it.id != itemId }
        replaceActiveVault(
            current.copy(
                items = newItems,
                updatedAt = nowMillis(),
                // Record a tombstone and mark it dirty so the deletion is synced.
                tombstones = (current.tombstones.filterNot { it.id == itemId } +
                    VaultTombstone(id = itemId, deletedAt = nowMillis())),
                dirtyItemIds = (current.dirtyItemIds + itemId).distinct()
            )
        )
    }

    fun getAllTags(): List<VaultItemTag> = synchronized(lock) {
        getActiveVaultOrNull()?.items.orEmpty().flatMap { it.tags }.distinct()
    }

    // --- Notification batching --------------------------------------------

    /** Begins coalescing change notifications. Must be paired with [endNotificationBatch]. */
    fun beginNotificationBatch() = synchronized(lock) { notificationBatchDepth++ }

    /** Ends a batch; emits a single coalesced change if any occurred while batching. */
    fun endNotificationBatch() = synchronized(lock) {
        if (notificationBatchDepth > 0) notificationBatchDepth--
        if (notificationBatchDepth == 0 && pendingNotification) {
            pendingNotification = false
            _activeVaultChanges.tryEmit(Unit)
        }
    }

    private fun notifyChange() {
        if (notificationBatchDepth > 0) {
            pendingNotification = true
        } else {
            _activeVaultChanges.tryEmit(Unit)
        }
    }

    private fun requireActiveEntry(): Entry {
        val id = activeVaultId ?: error("No active vault loaded")
        return loaded[id] ?: error("Active vault id '$id' is not in the loaded map")
    }

    private fun nowMillis(): Long {
        @OptIn(kotlin.time.ExperimentalTime::class)
        return kotlin.time.Clock.System.now().toEpochMilliseconds()
    }

    // --- Sync support ------------------------------------------------------

    fun getDirtyItemIds(): List<String> = synchronized(lock) { getActiveVault().dirtyItemIds }

    fun getTombstones(): List<VaultTombstone> = synchronized(lock) { getActiveVault().tombstones }

    fun getLastSyncSeq(): Long = synchronized(lock) { getActiveVault().lastSyncSeq }

    fun setLastSyncSeq(seq: Long) = synchronized(lock) {
        replaceActiveVault(getActiveVault().copy(lastSyncSeq = seq))
    }

    /** Clears dirty flags for items that were successfully pushed. */
    fun clearDirty(ids: Collection<String>) = synchronized(lock) {
        if (ids.isEmpty()) return@synchronized
        val current = getActiveVault()
        replaceActiveVault(
            current.copy(dirtyItemIds = current.dirtyItemIds.filterNot { it in ids })
        )
    }

    /** Removes tombstones that were successfully pushed. */
    fun clearTombstones(ids: Collection<String>) = synchronized(lock) {
        if (ids.isEmpty()) return@synchronized
        val current = getActiveVault()
        replaceActiveVault(
            current.copy(tombstones = current.tombstones.filterNot { it.id in ids })
        )
    }

    /**
     * Applies an item received from the server. Does NOT mark it dirty since it
     * is already in sync with the server. Caller is responsible for last-write-wins.
     */
    fun applyRemoteUpsert(item: VaultItemModel) = synchronized(lock) {
        val current = getActiveVault()
        val existing = current.items.firstOrNull { it.id == item.id }
        val updatedItems = if (existing == null) {
            current.items + item
        } else {
            current.items.map { if (it.id == item.id) item else it }
        }
        replaceActiveVault(
            current.copy(
                items = updatedItems,
                dirtyItemIds = current.dirtyItemIds.filterNot { it == item.id },
                tombstones = current.tombstones.filterNot { it.id == item.id }
            )
        )
    }

    /** Applies a remote deletion without creating a new outgoing tombstone. */
    fun applyRemoteDelete(itemId: String) = synchronized(lock) {
        val current = getActiveVault()
        replaceActiveVault(
            current.copy(
                items = current.items.filter { it.id != itemId },
                dirtyItemIds = current.dirtyItemIds.filterNot { it == itemId },
                tombstones = current.tombstones.filterNot { it.id == itemId }
            )
        )
    }
}
