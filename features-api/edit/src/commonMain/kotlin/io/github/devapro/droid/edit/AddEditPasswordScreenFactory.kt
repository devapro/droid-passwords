package io.github.devapro.droid.edit

import androidx.compose.runtime.Composable
import io.github.devapro.droid.data.model.ItemModel
import io.github.devapro.droid.data.vault.VaultItemTag

interface AddEditPasswordScreenFactory {

    @Composable
    fun CreateAddEditPasswordScreen(
        item: ItemModel?,
        selectedTag: VaultItemTag?
    )
}