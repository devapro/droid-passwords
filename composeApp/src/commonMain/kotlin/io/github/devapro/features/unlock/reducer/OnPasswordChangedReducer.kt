package io.github.devapro.features.unlock.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.features.unlock.model.UnLockVaultScreenAction
import io.github.devapro.features.unlock.model.UnLockVaultScreenEvent
import io.github.devapro.features.unlock.model.UnLockVaultScreenState

class OnPasswordChangedReducer
    : Reducer<UnLockVaultScreenAction.OnPasswordChanged, UnLockVaultScreenState, UnLockVaultScreenAction, UnLockVaultScreenEvent> {

    override val actionClass = UnLockVaultScreenAction.OnPasswordChanged::class

    override suspend fun reduce(
        action: UnLockVaultScreenAction.OnPasswordChanged,
        getState: () -> UnLockVaultScreenState
    ): Reducer.Result<UnLockVaultScreenState, UnLockVaultScreenAction.OnPasswordChanged, UnLockVaultScreenEvent?> {
        val currentState = getState()

        return if (currentState is UnLockVaultScreenState.Loaded) {
            val isValid = action.password.isNotEmpty()
            val passwordError = if (action.password.isEmpty()) null else null // Clear error on input

            val newState = currentState.copy(
                password = action.password,
                passwordError = passwordError,
                isValid = isValid
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
} 