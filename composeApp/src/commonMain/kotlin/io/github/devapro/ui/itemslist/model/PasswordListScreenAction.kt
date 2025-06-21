package io.github.devapro.ui.itemslist.model

import io.github.devapro.model.ItemModel

sealed interface PasswordListScreenAction {
    data object InitScreen : PasswordListScreenAction

    data class OnSearchChanged(val query: String) : PasswordListScreenAction
    
    data object OnAddPasswordClicked : PasswordListScreenAction

    data class OnPasswordItemClicked(val item: ItemModel) : PasswordListScreenAction

    data class OnDeletePasswordClicked(val item: ItemModel) : PasswordListScreenAction

    data object OnImportExportClicked : PasswordListScreenAction

    data object OnSettingsClicked : PasswordListScreenAction

    data object OnRefresh : PasswordListScreenAction

    data object OnClearSearch : PasswordListScreenAction
} 