package io.github.devapro.droid.data.vault

import kotlinx.serialization.Serializable

@Serializable
data class VaultModel(
    val password: String,
    val items: List<VaultItemModel>,
    val name: String = "",
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    /**
     * Ids of items changed locally since the last successful sync push. Only
     * these are uploaded during an incremental sync.
     */
    val dirtyItemIds: List<String> = emptyList(),
    /**
     * Deletions waiting to be propagated to the server.
     */
    val tombstones: List<VaultTombstone> = emptyList(),
    /**
     * Highest server sequence number this device has already pulled. Used as the
     * cursor for incremental pulls (`/sync/changes?since=lastSyncSeq`).
     */
    val lastSyncSeq: Long = 0
)
