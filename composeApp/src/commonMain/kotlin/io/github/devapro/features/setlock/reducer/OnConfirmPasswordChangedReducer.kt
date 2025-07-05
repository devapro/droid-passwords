package io.github.devapro.features.setlock.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.setlock.model.SetLockPasswordScreenAction
import io.github.devapro.features.setlock.model.SetLockPasswordScreenEvent
import io.github.devapro.features.setlock.model.SetLockPasswordScreenState

class OnConfirmPasswordChangedReducer
    : Reducer<SetLockPasswordScreenAction.OnConfirmPasswordChanged, SetLockPasswordScreenState, SetLockPasswordScreenAction, SetLockPasswordScreenEvent> {

    override val actionClass = SetLockPasswordScreenAction.OnConfirmPasswordChanged::class

    override suspend fun reduce(
        action: SetLockPasswordScreenAction.OnConfirmPasswordChanged,
        getState: () -> SetLockPasswordScreenState
    ): Reducer.Result<SetLockPasswordScreenState, SetLockPasswordScreenAction.OnConfirmPasswordChanged, SetLockPasswordScreenEvent?> {
        val currentState = getState()

        return if (currentState is SetLockPasswordScreenState.Success) {
            val confirmPasswordError = if (action.password != currentState.newPassword) {
                "Passwords do not match"
            } else null

            val newState = currentState.copy(
                confirmPassword = action.password,
                confirmPasswordError = confirmPasswordError,
                isValid = validateForm(
                    currentPassword = currentState.currentPassword,
                    newPassword = currentState.newPassword,
                    confirmPassword = action.password,
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