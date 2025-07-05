package io.github.devapro.features.tags.model

import io.github.devapro.data.vault.VaultItemTag

data class TagItemModel(
    val id: String,
    val name: String,
    val count: Int,
    val type: TagItemType,
    val tag: VaultItemTag?
)