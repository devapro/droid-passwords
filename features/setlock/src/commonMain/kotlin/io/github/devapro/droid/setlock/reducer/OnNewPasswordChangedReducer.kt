package io.github.devapro.droid.setlock.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.setlock.model.SetLockPasswordScreenAction
import io.github.devapro.droid.setlock.model.SetLockPasswordScreenEvent
import io.github.devapro.droid.setlock.model.SetLockPasswordScreenState

class OnNewPasswordChangedReducer
    : Reducer<SetLockPasswordScreenAction.OnNewPasswordChanged, SetLockPasswordScreenState, SetLockPasswordScreenAction, SetLockPasswordScreenEvent> {

    override val actionClass = SetLockPasswordScreenAction.OnNewPasswordChanged::class

    override suspend fun reduce(
        action: SetLockPasswordScreenAction.OnNewPasswordChanged,
        getState: () -> SetLockPasswordScreenState
    ): Reducer.Result<SetLockPasswordScreenState, SetLockPasswordScreenAction.OnNewPasswordChanged, SetLockPasswordScreenEvent?> {
        val currentState = getState()

        return if (currentState is SetLockPasswordScreenState.Success) {
            val newPasswordError = if (action.password.isEmpty()) "Password cannot be empty" else null
            val confirmPasswordError = if (currentState.confirmPassword.isNotEmpty() && action.password != currentState.confirmPassword) {
                "Passwords do not match"
            } else null

            val newState = currentState.copy(
                newPassword = action.password,
                newPasswordError = newPasswordError,
                confirmPasswordError = confirmPasswordError,
                isValid = validateForm(
                    currentPassword = currentState.currentPassword,
                    newPassword = action.password,
                    confirmPassword = currentState.confirmPassword,
                    hasExistingPassword = currentState.isVaultExists
                )
            )
            Reducer.Result(
                state = newState,
                action = null,
                event = null
            )
        } else {
            Reducer.Result(
                state = currentState,
                action = null,
                event = null
            )
        }
    }

    private fun validateForm(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String,
        hasExistingPassword: Boolean
    ): Boolean {
        if (hasExistingPassword && currentPassword.isEmpty()) return false
        if (newPassword.isEmpty()) return false
        if (newPassword != confirmPassword) return false
        return true
    }
} 