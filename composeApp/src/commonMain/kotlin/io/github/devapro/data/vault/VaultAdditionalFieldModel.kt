package io.github.devapro.data.vault

import kotlinx.serialization.Serializable

@Serializable
data class VaultAdditionalFieldModel(
    val name: String,
    val value: String
)