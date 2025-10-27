package io.github.devapro.droid.data.vault

class VaultRuntimeRepository {

    private var vaultModel = VaultModel(
        password = "",
        items = emptyList()
    )

    fun loadVault(vault: VaultModel) {
        vaultModel = vault
    }

    fun addOrUpdateVault(vaultItemModel: VaultItemModel) {
        val existingItemModel = vaultModel.items.firstOrNull { it.id == vaultItemModel.id }
        if (existingItemModel == null) {
            vaultModel = vaultModel.copy(items = vaultModel.items + vaultItemModel)
        } else {
            vaultModel.items.map {
                if (it.id == vaultItemModel.id) {
                    vaultItemModel
                } else {
                    it
                }
            }.also { updatedItems ->
                vaultModel = vaultModel.copy(items = updatedItems)
            }
        }
    }

    fun deleteVaultById(itemId: String) {
        vaultModel = vaultModel.copy(
            items = vaultModel.items.filter { it.id != itemId }
        )
    }

    fun getVault(): VaultModel {
        return vaultModel
    }

    fun getAllTags(): List<VaultItemTag> {
        return vaultModel.items.flatMap { it.tags }.distinct()
    }
}