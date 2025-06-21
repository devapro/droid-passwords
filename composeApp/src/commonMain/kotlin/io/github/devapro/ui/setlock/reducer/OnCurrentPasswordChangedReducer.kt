package io.github.devapro.ui.setlock.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.setlock.model.SetLockPasswordScreenAction
import io.github.devapro.ui.setlock.model.SetLockPasswordScreenEvent
import io.github.devapro.ui.setlock.model.SetLockPasswordScreenState

class OnCurrentPasswordChangedReducer
    : Reducer<SetLockPasswordScreenAction.OnCurrentPasswordChanged, SetLockPasswordScreenState, SetLockPasswordScreenAction, SetLockPasswordScreenEvent> {

    override val actionClass = SetLockPasswordScreenAction.OnCurrentPasswordChanged::class

    override suspend fun reduce(
        action: SetLockPasswordScreenAction.OnCurrentPasswordChanged,
        getState: () -> SetLockPasswordScreenState
    ): Reducer.Result<SetLockPasswordScreenState, SetLockPasswordScreenAction.OnCurrentPasswordChanged, SetLockPasswordScreenEvent?> {
        val currentState = getState()

        return if (currentState is SetLockPasswordScreenState.Success) {
            val newState = currentState.copy(
                currentPassword = action.password,
                currentPasswordError = null,
                isValid = validateForm(
                    currentPassword = action.password,
                    newPassword = currentState.newPassword,
                    confirmPassword = currentState.confirmPassword,
                    hasExistingPassword = currentState.hasExistingPassword
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