package io.github.devapro.ui.unlock.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.unlock.model.UnLockVaultScreenAction
import io.github.devapro.ui.unlock.model.UnLockVaultScreenEvent
import io.github.devapro.ui.unlock.model.UnLockVaultScreenState

class OnTogglePasswordVisibilityReducer
    : Reducer<UnLockVaultScreenAction.OnTogglePasswordVisibility, UnLockVaultScreenState, UnLockVaultScreenAction, UnLockVaultScreenEvent> {

    override val actionClass = UnLockVaultScreenAction.OnTogglePasswordVisibility::class

    override suspend fun reduce(
        action: UnLockVaultScreenAction.OnTogglePasswordVisibility,
        getState: () -> UnLockVaultScreenState
    ): Reducer.Result<UnLockVaultScreenState, UnLockVaultScreenAction.OnTogglePasswordVisibility, UnLockVaultScreenEvent?> {
        val currentState = getState()

        return if (currentState is UnLockVaultScreenState.Loaded) {
            Reducer.Result(
                state = currentState.copy(isPasswordVisible = !currentState.isPasswordVisible),
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