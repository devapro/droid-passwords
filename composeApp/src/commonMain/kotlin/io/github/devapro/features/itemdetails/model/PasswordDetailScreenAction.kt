package io.github.devapro.features.itemdetails.model

import io.github.devapro.droid.data.model.ItemModel

sealed interface PasswordDetailScreenAction {
    data class InitScreen(val item: ItemModel) : PasswordDetailScreenAction

    data object OnEditClicked : PasswordDetailScreenAction
    
    data object OnDeleteClicked : PasswordDetailScreenAction

    data object OnDeleteConfirmed : PasswordDetailScreenAction

    data object OnDeleteCancelled : PasswordDetailScreenAction

    data object OnBackClicked : PasswordDetailScreenAction

    data object OnTogglePasswordVisibility : PasswordDetailScreenAction

    data class OnCopyField(val fieldName: String, val value: String) : PasswordDetailScreenAction

    data object OnShareClicked : PasswordDetailScreenAction
} 