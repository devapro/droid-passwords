package io.github.devapro.droid.data.vault

import kotlinx.serialization.Serializable

@Serializable
data class VaultModel(
    val password: String,
    val items: List<VaultItemModel>,
    val name: String = "",
    /**
     * Stable unique identity of this vault file, generated once when the vault is
     * first created and carried inside the encrypted file thereafter. Equals the
     * vault's [VaultDescriptor.id] (and the server namespace id). Empty only for
     * legacy files created before this field existed — those are stamped on first
     * unlock. Lets a file identify itself even if the local registry is lost, and
     * distinguishes a genuinely new file from an update to an existing one.
     */
    val vaultId: String = "",
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
