package io.github.devapro.model

data class ItemModel(
    val id: String,
    val title: String,
    val description: String,
    val url: String,
    val password: String,
    val username: String,
    val additionalFields: List<AdditionalFieldsModel>
)