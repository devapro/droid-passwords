package io.github.devapro.droid.data.vault

import kotlinx.serialization.Serializable

@Serializable
data class VaultItemTag(
    val id: String,
    val title: String
)
