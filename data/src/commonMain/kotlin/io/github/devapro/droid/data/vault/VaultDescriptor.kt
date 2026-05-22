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
) {
    companion object {
        @OptIn(ExperimentalUuidApi::class)
        fun newId(): String = Uuid.random().toHexDashString()

        fun newFileName(id: String): String = "vault-$id.data"
    }
}
