package io.github.devapro.droid.data.vault

import kotlinx.serialization.Serializable

@Serializable
data class VaultModel(
    val password: String,
    val items: List<VaultItemModel>,
    val name: String = "",
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
)
