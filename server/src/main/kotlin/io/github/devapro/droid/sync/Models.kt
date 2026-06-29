package io.github.devapro.droid.sync

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
 * One item as sent by the client during a push. The [payload] is an opaque,
 * end-to-end encrypted blob (base64). The server never decrypts it.
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

/**
 * One item returned by the server during an incremental pull.
 */
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
 * A single vault namespace owned by the authenticated account. Items are scoped
 * by (userId, vaultId), so one account can hold several independent vaults.
 *
 * [name] is an opaque, end-to-end encrypted blob (base64) of the vault's display
 * name — the server never decrypts it. [deleted] marks a whole-vault tombstone so
 * other devices remove the vault locally.
 */
@Serializable
data class VaultSummary(
    val vaultId: String,
    val latestSeq: Long,
    val updatedAt: Long,
    val name: String? = null,
    val nameUpdatedAt: Long = 0,
    val deleted: Boolean = false
)

@Serializable
data class VaultListResponse(
    val vaults: List<VaultSummary>
)

/**
 * Sets per-vault metadata. [name] is the encrypted display-name blob, resolved
 * last-write-wins by [nameUpdatedAt]. [deleted] tombstones the whole vault.
 */
@Serializable
data class VaultMetaRequest(
    val name: String? = null,
    val nameUpdatedAt: Long = 0,
    val deleted: Boolean = false,
    val updatedAt: Long = 0
)

@Serializable
data class ErrorResponse(
    val error: String
)
