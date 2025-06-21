package io.github.devapro.ui.edit.model

import io.github.devapro.model.ItemModel

sealed interface AddEditPasswordScreenEvent {

    data object NavigateBack : AddEditPasswordScreenEvent

    data class SaveSuccess(val item: ItemModel) : AddEditPasswordScreenEvent

    data class SaveError(val message: String) : AddEditPasswordScreenEvent

    data class DeleteSuccess(val itemId: String) : AddEditPasswordScreenEvent

    data class DeleteError(val message: String) : AddEditPasswordScreenEvent

    data class ShowMessage(val message: String) : AddEditPasswordScreenEvent

    data class GeneratedPassword(val password: String) : AddEditPasswordScreenEvent
} 