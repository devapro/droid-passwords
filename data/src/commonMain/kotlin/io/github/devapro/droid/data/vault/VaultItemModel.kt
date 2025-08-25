package io.github.devapro.droid.data.vault

import kotlinx.serialization.Serializable

@Serializable
data class VaultItemModel(
    val id: String,
    val title: String,
    val username: String,
    val password: String,
    val tags: List<VaultItemTag> = emptyList(),
    val url: String? = null,
    val description: String? = null,
    val additionalFields: List<VaultAdditionalFieldModel> = emptyList()
)
