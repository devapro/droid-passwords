package io.github.devapro.ui.itemslist.model

import io.github.devapro.model.ItemModel

sealed interface PasswordListScreenEvent {

    data object NavigateToAddPassword : PasswordListScreenEvent

    data class NavigateToPasswordDetail(val item: ItemModel) : PasswordListScreenEvent

    data object NavigateToImportExport : PasswordListScreenEvent

    data object NavigateToSettings : PasswordListScreenEvent

    data class DeletePassword(val item: ItemModel) : PasswordListScreenEvent

    data object RefreshPasswordList : PasswordListScreenEvent
} 