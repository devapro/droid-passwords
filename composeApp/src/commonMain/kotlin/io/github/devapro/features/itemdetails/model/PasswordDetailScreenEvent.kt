package io.github.devapro.features.itemdetails.model

import io.github.devapro.droid.data.model.ItemModel

sealed interface PasswordDetailScreenEvent {

    data object NavigateBack : PasswordDetailScreenEvent

    data class NavigateToEdit(val item: ItemModel) : PasswordDetailScreenEvent

    data class CopyToClipboard(val fieldName: String, val value: String) : PasswordDetailScreenEvent

    data class ShareItem(val item: ItemModel) : PasswordDetailScreenEvent

    data class DeleteItem(val item: ItemModel) : PasswordDetailScreenEvent

    data class ShowMessage(val message: String) : PasswordDetailScreenEvent
} 