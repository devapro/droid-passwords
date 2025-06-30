package io.github.devapro.ui.itemslist.mapper

import io.github.devapro.data.vault.VaultItemModel
import io.github.devapro.model.AdditionalFieldsModel
import io.github.devapro.model.ItemModel

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
                }
            )
        }
    }
}