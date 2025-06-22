package io.github.devapro.data.vault

import kotlinx.serialization.Serializable

@Serializable
data class VaultModel(
    val items: List<VaultItemModel>
)
