package io.github.devapro.droid.edit.model

sealed interface AddEditPasswordScreenEvent {

    data object NavigateBack : AddEditPasswordScreenEvent

    data object SaveSuccess : AddEditPasswordScreenEvent

    data class DeleteSuccess(val itemId: String) : AddEditPasswordScreenEvent

    data class ShowMessage(val message: String) : AddEditPasswordScreenEvent

    data class GeneratedPassword(val password: String) : AddEditPasswordScreenEvent
} 