package io.github.devapro.droid.itemslist.model

import io.github.devapro.droid.data.model.ItemModel
import io.github.devapro.droid.data.vault.VaultItemTag
import io.github.devapro.droid.itemlist.PasswordTagFilterType

sealed interface PasswordListScreenAction {
    data class InitScreen(
        val tagFilterType: PasswordTagFilterType,
        val tag: VaultItemTag?
    ) : PasswordListScreenAction

    data object OnBackClicked : PasswordListScreenAction

    data class OnSearchChanged(val query: String) : PasswordListScreenAction
    
    data object OnAddPasswordClicked : PasswordListScreenAction

    data class OnPasswordItemClicked(val item: ItemModel) : PasswordListScreenAction

    data class OnDeletePasswordClicked(val item: ItemModel) : PasswordListScreenAction

    data object OnExportClicked : PasswordListScreenAction

    data object OnSettingsClicked : PasswordListScreenAction

    data object OnClearSearch : PasswordListScreenAction
} 