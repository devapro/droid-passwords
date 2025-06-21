package io.github.devapro.ui.itemdetails.model

import io.github.devapro.model.ItemModel

sealed interface PasswordDetailScreenAction {
    data class InitScreen(val item: ItemModel) : PasswordDetailScreenAction

    data object OnEditClicked : PasswordDetailScreenAction
    
    data object OnDeleteClicked : PasswordDetailScreenAction

    data object OnBackClicked : PasswordDetailScreenAction

    data object OnTogglePasswordVisibility : PasswordDetailScreenAction

    data class OnCopyField(val fieldName: String, val value: String) : PasswordDetailScreenAction

    data object OnShareClicked : PasswordDetailScreenAction

    data object OnFavoriteClicked : PasswordDetailScreenAction
} 