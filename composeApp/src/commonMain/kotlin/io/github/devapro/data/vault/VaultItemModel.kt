package io.github.devapro.data.vault

import kotlinx.serialization.Serializable

@Serializable
data class VaultItemModel(
    val id: String,
    val title: String,
    val username: String,
    val password: String,
    val url: String? = null,
    val notes: String? = null,
    val createdAt: Long,
    val updatedAt: Long
)
