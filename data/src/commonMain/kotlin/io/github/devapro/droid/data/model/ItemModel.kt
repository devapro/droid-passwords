package io.github.devapro.droid.data.model

import io.github.devapro.droid.data.vault.VaultItemTag

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