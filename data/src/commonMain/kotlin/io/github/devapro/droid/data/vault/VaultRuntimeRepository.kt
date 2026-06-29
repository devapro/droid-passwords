package io.github.devapro.droid.data.vault

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class VaultRuntimeRepository {

    private var vaultModel = VaultModel(
        password = "",
        items = emptyList()
    )

    fun loadVault(vault: VaultModel) {
        vaultModel = vault
    }

    fun addOrUpdateVault(vaultItemModel: VaultItemModel) {
        val existingItemModel = vaultModel.items.firstOrNull { it.id == vaultItemModel.id }
        val updatedItems = if (existingItemModel == null) {
            vaultModel.items + vaultItemModel
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
    }

    @OptIn(ExperimentalTime::class)
    fun deleteVaultById(itemId: String) {
        val now = Clock.System.now().toEpochMilliseconds()
        vaultModel = vaultModel.copy(
            items = vaultModel.items.filter { it.id != itemId },
            // Record a tombstone and mark it dirty so the deletion is synced.
            tombstones = (vaultModel.tombstones.filterNot { it.id == itemId } +
                VaultTombstone(id = itemId, deletedAt = now)),
            dirtyItemIds = (vaultModel.dirtyItemIds + itemId).distinct()
        )
    }

    fun getVault(): VaultModel {
        return vaultModel
    }

    fun getAllTags(): List<VaultItemTag> {
        return vaultModel.items.flatMap { it.tags }.distinct()
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
