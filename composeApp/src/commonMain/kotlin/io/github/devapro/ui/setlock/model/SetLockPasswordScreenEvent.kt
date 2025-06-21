package io.github.devapro.ui.setlock.model

sealed interface SetLockPasswordScreenEvent {

    data object NavigateBack : SetLockPasswordScreenEvent

    data class SavePassword(
        val oldPassword: String?,
        val newPassword: String
    ) : SetLockPasswordScreenEvent

    data object RemovePassword : SetLockPasswordScreenEvent

    data class ShowError(val message: String) : SetLockPasswordScreenEvent

    data object ShowSuccess : SetLockPasswordScreenEvent
} 