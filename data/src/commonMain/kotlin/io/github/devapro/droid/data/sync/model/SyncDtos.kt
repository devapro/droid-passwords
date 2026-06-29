package io.github.devapro.droid.data.sync.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val username: String,
    val password: String
)

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val token: String,
    val username: String
)

/**
 * A single item uploaded during a push. [payload] is the base64-encoded,
 * end-to-end encrypted item blob (or null for deletions).
 */
@Serializable
data class PushItem(
    val id: String,
    val updatedAt: Long,
    val deleted: Boolean = false,
    val payload: String? = null
)

@Serializable
data class PushRequest(
    val items: List<PushItem>
)

@Serializable
data class PushResultItem(
    val id: String,
    val seq: Long,
    val stored: Boolean,
    val serverUpdatedAt: Long
)

@Serializable
data class PushResponse(
    val results: List<PushResultItem>,
    val latestSeq: Long
)

@Serializable
data class ChangeItem(
    val id: String,
    val seq: Long,
    val updatedAt: Long,
    val deleted: Boolean,
    val payload: String? = null
)

@Serializable
data class ChangesResponse(
    val items: List<ChangeItem>,
    val latestSeq: Long
)

/**
 * A single vault namespace known to the server for the authenticated account.
 * Used by a fresh device to discover which vaults are available to restore.
 */
@Serializable
data class VaultSummary(
    val vaultId: String,
    val latestSeq: Long,
    val updatedAt: Long,
    /** Opaque, end-to-end encrypted (base64) blob of the vault's display name, or null. */
    val name: String? = null,
    val nameUpdatedAt: Long = 0,
    /** Whole-vault tombstone: other devices remove the vault locally. */
    val deleted: Boolean = false
)

@Serializable
data class VaultListResponse(
    val vaults: List<VaultSummary>
)

/** Sets per-vault metadata: encrypted [name] (LWW by [nameUpdatedAt]) and [deleted]. */
@Serializable
data class VaultMetaRequest(
    val name: String? = null,
    val nameUpdatedAt: Long = 0,
    val deleted: Boolean = false,
    val updatedAt: Long = 0
)
