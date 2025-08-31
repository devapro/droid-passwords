package io.github.devapro.droid.tags.model

import io.github.devapro.droid.data.vault.VaultItemTag

data class TagItemModel(
    val id: String,
    val name: String,
    val count: Int,
    val type: TagItemType,
    val tag: VaultItemTag?
)