package io.github.devapro.droid.data.vault

import kotlinx.serialization.Serializable

@Serializable
data class VaultAdditionalFieldModel(
    val name: String,
    val value: String
)