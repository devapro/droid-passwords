package io.github.devapro.droid.setlock.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.setlock.model.SetLockPasswordScreenAction
import io.github.devapro.droid.setlock.model.SetLockPasswordScreenEvent
import io.github.devapro.droid.setlock.model.SetLockPasswordScreenState

class OnVaultNameChangedReducer
    : Reducer<SetLockPasswordScreenAction.OnVaultNameChanged, SetLockPasswordScreenState, SetLockPasswordScreenAction, SetLockPasswordScreenEvent> {

    override val actionClass = SetLockPasswordScreenAction.OnVaultNameChanged::class

    override suspend fun reduce(
        action: SetLockPasswordScreenAction.OnVaultNameChanged,
        getState: () -> SetLockPasswordScreenState
    ): Reducer.Result<SetLockPasswordScreenState, SetLockPasswordScreenAction.OnVaultNameChanged, SetLockPasswordScreenEvent?> {
        val currentState = getState()

        return if (currentState is SetLockPasswordScreenState.Success) {
            val trimmed = action.name
            val nameError = if (trimmed.isBlank()) "Vault name cannot be empty" else null
            val newState = currentState.copy(
                vaultName = trimmed,
                vaultNameError = nameError,
                isValid = validateForm(
                    vaultName = trimmed,
                    currentPassword = currentState.currentPassword,
                    newPassword = currentState.newPassword,
                    confirmPassword = currentState.confirmPassword,
                    hasExistingPassword = currentState.isVaultExists
                )
            )
            Reducer.Result(state = newState, action = null, event = null)
        } else {
            Reducer.Result(state = currentState, action = null, event = null)
        }
    }

    private fun validateForm(
        vaultName: String,
        currentPassword: String,
        newPassword: String,
        confirmPassword: String,
        hasExistingPassword: Boolean
    ): Boolean {
        if (vaultName.isBlank()) return false
        if (hasExistingPassword && currentPassword.isEmpty()) return false
        if (newPassword.isEmpty()) return false
        if (newPassword != confirmPassword) return false
        return true
    }
}
