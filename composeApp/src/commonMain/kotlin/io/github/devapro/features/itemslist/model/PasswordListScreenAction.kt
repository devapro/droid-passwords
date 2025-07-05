package io.github.devapro.features.itemslist.model

import io.github.devapro.data.vault.VaultItemTag
import io.github.devapro.features.itemslist.navigation.PasswordTagFilterType
import io.github.devapro.model.ItemModel

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

    data object OnImportExportClicked : PasswordListScreenAction

    data object OnSettingsClicked : PasswordListScreenAction

    data object OnClearSearch : PasswordListScreenAction
} 