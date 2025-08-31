package io.github.devapro.droid.itemslist.model

import io.github.devapro.droid.data.model.ItemModel

sealed interface PasswordListScreenEvent {

    data object NavigateToAddPassword : PasswordListScreenEvent

    data class NavigateToPasswordDetail(val item: ItemModel) : PasswordListScreenEvent

    data object NavigateToExport : PasswordListScreenEvent

    data object NavigateToSettings : PasswordListScreenEvent

    data class DeletePassword(val item: ItemModel) : PasswordListScreenEvent

    data object OnBackClicked : PasswordListScreenEvent
} 