package io.github.devapro.droid.itemslist.model

import io.github.devapro.droid.data.model.ItemModel
import io.github.devapro.droid.data.vault.VaultItemTag

sealed interface PasswordListScreenEvent {

    data class NavigateToAddPassword(
        val tag: VaultItemTag?
    ) : PasswordListScreenEvent

    data class NavigateToPasswordDetail(val item: ItemModel) : PasswordListScreenEvent

    data object NavigateToExport : PasswordListScreenEvent

    data object NavigateToSettings : PasswordListScreenEvent

    data class DeletePassword(val item: ItemModel) : PasswordListScreenEvent

    data object OnBackClicked : PasswordListScreenEvent
} 