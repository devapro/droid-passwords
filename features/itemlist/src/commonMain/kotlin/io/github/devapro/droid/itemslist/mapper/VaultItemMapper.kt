package io.github.devapro.droid.itemslist.mapper

import io.github.devapro.droid.data.model.AdditionalFieldsModel
import io.github.devapro.droid.data.model.ItemModel
import io.github.devapro.droid.data.vault.VaultItemModel

class VaultItemMapper {

    fun map(vaultItems: List<VaultItemModel>): List<ItemModel> {
        return vaultItems.map { vaultItem ->
            ItemModel(
                id = vaultItem.id,
                title = vaultItem.title,
                description = vaultItem.description.orEmpty(),
                url = vaultItem.url.orEmpty(),
                password = vaultItem.password,
                username = vaultItem.username,
                additionalFields = vaultItem.additionalFields.map { additionalField ->
                    AdditionalFieldsModel(
                        name = additionalField.name,
                        value = additionalField.value
                    )
                },
                tags = vaultItem.tags
            )
        }
    }
}