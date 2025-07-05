package io.github.devapro.model

import io.github.devapro.data.vault.VaultItemTag

data class ItemModel(
    val id: String,
    val title: String,
    val description: String,
    val url: String,
    val password: String,
    val username: String,
    val additionalFields: List<AdditionalFieldsModel>,
    val tags: List<VaultItemTag>
)