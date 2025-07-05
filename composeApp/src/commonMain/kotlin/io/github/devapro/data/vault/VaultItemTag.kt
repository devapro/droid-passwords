package io.github.devapro.data.vault

import kotlinx.serialization.Serializable

@Serializable
data class VaultItemTag(
    val id: String,
    val title: String
)
