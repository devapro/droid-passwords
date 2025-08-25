package io.github.devapro.droid.edit.model

sealed interface AddEditPasswordScreenEvent {

    data object NavigateBack : AddEditPasswordScreenEvent

    data object SaveSuccess : AddEditPasswordScreenEvent

    data class SaveError(val message: String) : AddEditPasswordScreenEvent

    data class DeleteSuccess(val itemId: String) : AddEditPasswordScreenEvent

    data class DeleteError(val message: String) : AddEditPasswordScreenEvent

    data class GeneratedPassword(val password: String) : AddEditPasswordScreenEvent
} 