package io.github.devapro.data.vault

class VaultRuntimeRepository {

    private var vaultModel = VaultModel(
        items = emptyList()
    )

    fun loadVault(vault: VaultModel) {
        vaultModel = vault
    }

    fun updateVault(vaultItemModel: VaultItemModel) {
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

    fun deleteVault(vaultItemModel: VaultItemModel) {
        vaultModel = vaultModel.copy(
            items = vaultModel.items.filter { it.id != vaultItemModel.id }
        )
    }
}