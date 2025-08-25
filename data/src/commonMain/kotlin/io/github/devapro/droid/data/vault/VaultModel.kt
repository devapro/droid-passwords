package io.github.devapro.droid.data.vault

import kotlinx.serialization.Serializable

@Serializable
data class VaultModel(
    val password: String,
    val items: List<VaultItemModel>
)
