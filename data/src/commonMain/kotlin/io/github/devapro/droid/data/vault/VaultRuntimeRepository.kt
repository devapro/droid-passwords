package io.github.devapro.droid.data.vault

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
    fun updateItemMetadata(item: VaultItemModel) {
        val current = getActiveVault()
        if (current.items.none { it.id == item.id }) return
        replaceActiveVault(
            current.copy(
                items = current.items.map { if (it.id == item.id) item else it }
            )
        )
    }

    fun deleteVaultById(itemId: String) {
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

    fun getDirtyItemIds(): List<String> = getActiveVault().dirtyItemIds

    fun getTombstones(): List<VaultTombstone> = getActiveVault().tombstones

    fun getLastSyncSeq(): Long = getActiveVault().lastSyncSeq

    fun setLastSyncSeq(seq: Long) {
        replaceActiveVault(getActiveVault().copy(lastSyncSeq = seq))
    }

    /** Clears dirty flags for items that were successfully pushed. */
    fun clearDirty(ids: Collection<String>) {
        if (ids.isEmpty()) return
        val current = getActiveVault()
        replaceActiveVault(
            current.copy(dirtyItemIds = current.dirtyItemIds.filterNot { it in ids })
        )
    }

    /** Removes tombstones that were successfully pushed. */
    fun clearTombstones(ids: Collection<String>) {
        if (ids.isEmpty()) return
        val current = getActiveVault()
        replaceActiveVault(
            current.copy(tombstones = current.tombstones.filterNot { it.id in ids })
        )
    }

    /**
     * Applies an item received from the server. Does NOT mark it dirty since it
     * is already in sync with the server. Caller is responsible for last-write-wins.
     */
    fun applyRemoteUpsert(item: VaultItemModel) {
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
    fun applyRemoteDelete(itemId: String) {
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
