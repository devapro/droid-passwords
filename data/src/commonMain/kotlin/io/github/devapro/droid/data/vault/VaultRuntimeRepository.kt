package io.github.devapro.droid.data.vault

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class VaultRuntimeRepository {

    private data class Entry(val descriptor: VaultDescriptor, var vault: VaultModel)

    private val loaded = mutableMapOf<String, Entry>()
    private var activeVaultId: String? = null

    private val _activeVaultChanges = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val activeVaultChanges: SharedFlow<Unit> = _activeVaultChanges.asSharedFlow()

    fun loadVault(descriptor: VaultDescriptor, vault: VaultModel) {
        loaded[descriptor.id] = Entry(descriptor, vault)
        if (activeVaultId == null) activeVaultId = descriptor.id
        _activeVaultChanges.tryEmit(Unit)
    }

    fun setActiveVault(id: String): Boolean {
        if (!loaded.containsKey(id)) return false
        activeVaultId = id
        _activeVaultChanges.tryEmit(Unit)
        return true
    }

    fun getActiveVaultId(): String? = activeVaultId

    fun isLoaded(id: String): Boolean = loaded.containsKey(id)

    fun unloadVault(id: String) {
        loaded.remove(id)
        if (activeVaultId == id) {
            activeVaultId = loaded.keys.firstOrNull()
            _activeVaultChanges.tryEmit(Unit)
        }
    }

    fun unloadAll() {
        loaded.clear()
        activeVaultId = null
        _activeVaultChanges.tryEmit(Unit)
    }

    fun addOrUpdateVault(vaultItemModel: VaultItemModel) {
        val existingItemModel = vaultModel.items.firstOrNull { it.id == vaultItemModel.id }
        val updatedItems = if (existingItemModel == null) {
            vaultModel.items + vaultItemModel
    fun getVault(): VaultModel = getActiveVault()

    fun getActiveVault(): VaultModel = requireActiveEntry().vault

    fun getActiveDescriptor(): VaultDescriptor = requireActiveEntry().descriptor

    fun getVault(id: String): VaultModel? = loaded[id]?.vault

    fun getDescriptor(id: String): VaultDescriptor? = loaded[id]?.descriptor

    fun listLoaded(): List<VaultDescriptor> = loaded.values.map { it.descriptor }

    fun replaceActiveVault(vault: VaultModel) {
        val entry = requireActiveEntry()
        entry.vault = vault
        _activeVaultChanges.tryEmit(Unit)
    }

    fun replaceActiveDescriptor(descriptor: VaultDescriptor) {
        val entry = requireActiveEntry()
        val oldId = entry.descriptor.id
        if (oldId != descriptor.id) {
            loaded.remove(oldId)
            activeVaultId = descriptor.id
        }
        loaded[descriptor.id] = entry.copy(descriptor = descriptor)
    }

    fun addOrUpdateVault(item: VaultItemModel) {
        val current = getActiveVault()
        val newItems = if (current.items.any { it.id == item.id }) {
            current.items.map { if (it.id == item.id) item else it }
        } else {
            vaultModel.items.map {
                if (it.id == vaultItemModel.id) vaultItemModel else it
            }
        }
        vaultModel = vaultModel.copy(
            items = updatedItems,
            // Mark as locally changed and resurrect any pending tombstone.
            dirtyItemIds = (vaultModel.dirtyItemIds + vaultItemModel.id).distinct(),
            tombstones = vaultModel.tombstones.filterNot { it.id == vaultItemModel.id }
        )
    }

    /**
     * Updates an existing item in place WITHOUT marking it dirty. For local-only
     * metadata changes (e.g. bumping `lastUsedAt` when an item is merely viewed)
     * that should not trigger a sync upload.
     */
    fun updateItemMetadata(vaultItemModel: VaultItemModel) {
        if (vaultModel.items.none { it.id == vaultItemModel.id }) return
        vaultModel = vaultModel.copy(
            items = vaultModel.items.map {
                if (it.id == vaultItemModel.id) vaultItemModel else it
            }
        )
            current.items + item
        }
        replaceActiveVault(current.copy(items = newItems, updatedAt = nowMillis()))
    }

    @OptIn(ExperimentalTime::class)
    fun deleteVaultById(itemId: String) {
        val current = getActiveVault()
        val newItems = current.items.filter { it.id != itemId }
        replaceActiveVault(current.copy(items = newItems, updatedAt = nowMillis()))
        val now = Clock.System.now().toEpochMilliseconds()
        vaultModel = vaultModel.copy(
            items = vaultModel.items.filter { it.id != itemId },
            // Record a tombstone and mark it dirty so the deletion is synced.
            tombstones = (vaultModel.tombstones.filterNot { it.id == itemId } +
                VaultTombstone(id = itemId, deletedAt = now)),
            dirtyItemIds = (vaultModel.dirtyItemIds + itemId).distinct()
        )
    }

    fun getAllTags(): List<VaultItemTag> =
        getActiveVault().items.flatMap { it.tags }.distinct()

    private fun requireActiveEntry(): Entry {
        val id = activeVaultId ?: error("No active vault loaded")
        return loaded[id] ?: error("Active vault id '$id' is not in the loaded map")
    }

    private fun nowMillis(): Long {
        @OptIn(kotlin.time.ExperimentalTime::class)
        return kotlin.time.Clock.System.now().toEpochMilliseconds()
    }

    // --- Sync support ------------------------------------------------------

    fun getDirtyItemIds(): List<String> = vaultModel.dirtyItemIds

    fun getTombstones(): List<VaultTombstone> = vaultModel.tombstones

    fun getLastSyncSeq(): Long = vaultModel.lastSyncSeq

    fun setLastSyncSeq(seq: Long) {
        vaultModel = vaultModel.copy(lastSyncSeq = seq)
    }

    /** Clears dirty flags for items that were successfully pushed. */
    fun clearDirty(ids: Collection<String>) {
        if (ids.isEmpty()) return
        vaultModel = vaultModel.copy(
            dirtyItemIds = vaultModel.dirtyItemIds.filterNot { it in ids }
        )
    }

    /** Removes tombstones that were successfully pushed. */
    fun clearTombstones(ids: Collection<String>) {
        if (ids.isEmpty()) return
        vaultModel = vaultModel.copy(
            tombstones = vaultModel.tombstones.filterNot { it.id in ids }
        )
    }

    /**
     * Applies an item received from the server. Does NOT mark it dirty since it
     * is already in sync with the server. Caller is responsible for last-write-wins.
     */
    fun applyRemoteUpsert(vaultItemModel: VaultItemModel) {
        val existing = vaultModel.items.firstOrNull { it.id == vaultItemModel.id }
        val updatedItems = if (existing == null) {
            vaultModel.items + vaultItemModel
        } else {
            vaultModel.items.map { if (it.id == vaultItemModel.id) vaultItemModel else it }
        }
        vaultModel = vaultModel.copy(
            items = updatedItems,
            dirtyItemIds = vaultModel.dirtyItemIds.filterNot { it == vaultItemModel.id },
            tombstones = vaultModel.tombstones.filterNot { it.id == vaultItemModel.id }
        )
    }

    /** Applies a remote deletion without creating a new outgoing tombstone. */
    fun applyRemoteDelete(itemId: String) {
        vaultModel = vaultModel.copy(
            items = vaultModel.items.filter { it.id != itemId },
            dirtyItemIds = vaultModel.dirtyItemIds.filterNot { it == itemId },
            tombstones = vaultModel.tombstones.filterNot { it.id == itemId }
        )
    }
}
