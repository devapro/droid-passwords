package io.github.devapro.ui.setlock.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.setlock.model.SetLockPasswordScreenAction
import io.github.devapro.ui.setlock.model.SetLockPasswordScreenEvent
import io.github.devapro.ui.setlock.model.SetLockPasswordScreenState

class InitScreenReducer
    : Reducer<SetLockPasswordScreenAction.InitScreen, SetLockPasswordScreenState, SetLockPasswordScreenAction, SetLockPasswordScreenEvent> {

    override val actionClass = SetLockPasswordScreenAction.InitScreen::class

    override suspend fun reduce(
        action: SetLockPasswordScreenAction.InitScreen,
        getState: () -> SetLockPasswordScreenState
    ): Reducer.Result<SetLockPasswordScreenState, SetLockPasswordScreenAction.InitScreen, SetLockPasswordScreenEvent?> {
        return Reducer.Result(
            state = SetLockPasswordScreenState.Success(
                hasExistingPassword = false, // Will be determined by business logic
                currentPassword = "",
                newPassword = "",
                confirmPassword = "",
                isCurrentPasswordVisible = false,
                isNewPasswordVisible = false,
                isConfirmPasswordVisible = false,
                isProcessing = false,
                currentPasswordError = null,
                newPasswordError = null,
                confirmPasswordError = null,
                isValid = false
            ),
            action = null,
            event = null
        )
    }
} 