package io.github.devapro.features.itemslist.model

import io.github.devapro.model.ItemModel

sealed interface PasswordListScreenEvent {

    data object NavigateToAddPassword : PasswordListScreenEvent

    data class NavigateToPasswordDetail(val item: ItemModel) : PasswordListScreenEvent

    data object NavigateToExport : PasswordListScreenEvent

    data object NavigateToSettings : PasswordListScreenEvent

    data class DeletePassword(val item: ItemModel) : PasswordListScreenEvent

    data object OnBackClicked : PasswordListScreenEvent
} 