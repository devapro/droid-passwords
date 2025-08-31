package io.github.devapro.droid.setlock.model

sealed interface SetLockPasswordScreenAction {
    data object InitScreen : SetLockPasswordScreenAction

    data class OnCurrentPasswordChanged(val password: String) : SetLockPasswordScreenAction
    
    data class OnNewPasswordChanged(val password: String) : SetLockPasswordScreenAction
    
    data class OnConfirmPasswordChanged(val password: String) : SetLockPasswordScreenAction

    data object OnToggleCurrentPasswordVisibility : SetLockPasswordScreenAction

    data object OnToggleNewPasswordVisibility : SetLockPasswordScreenAction

    data object OnToggleConfirmPasswordVisibility : SetLockPasswordScreenAction

    data object OnSaveClicked : SetLockPasswordScreenAction

    data object OnBackClicked : SetLockPasswordScreenAction

    data object OnRemovePasswordClicked : SetLockPasswordScreenAction
} 