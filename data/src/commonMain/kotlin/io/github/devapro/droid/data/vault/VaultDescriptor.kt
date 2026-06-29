package io.github.devapro.droid.data.vault

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class VaultDescriptor(
    val id: String,
    val name: String,
    val fileName: String,
    val createdAt: Long,
    val updatedAt: Long,
    /**
     * True when this vault was discovered on the sync server but its contents have
     * not been downloaded yet — there is no local file. It shows in the vault list
     * as a locked vault; the master password is requested the first time the user
     * opens it, which triggers the download (see [io.github.devapro.droid.data.sync.SyncManager.downloadPendingVault]).
     */
    val pendingRestore: Boolean = false,
    /**
     * For a [pendingRestore] vault, the server's opaque end-to-end-encrypted (base64)
     * display-name blob. Decrypted to the real [name] once the master password is
     * supplied on first open; null otherwise.
     */
    val encryptedName: String? = null,
) {
    companion object {
        @OptIn(ExperimentalUuidApi::class)
        fun newId(): String = Uuid.random().toHexDashString()

        fun newFileName(id: String): String = "vault-$id.data"
    }
}
