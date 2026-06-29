package io.github.devapro.droid.data.vault

import kotlinx.serialization.Serializable

/**
 * Records the deletion of an item so the deletion can be propagated to the
 * sync server (and other devices) on the next sync. Tombstones are pruned once
 * they have been successfully pushed.
 */
@Serializable
data class VaultTombstone(
    val id: String,
    val deletedAt: Long
)
