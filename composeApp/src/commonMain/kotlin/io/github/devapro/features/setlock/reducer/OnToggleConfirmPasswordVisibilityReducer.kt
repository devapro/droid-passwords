package io.github.devapro.features.setlock.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.setlock.model.SetLockPasswordScreenAction
import io.github.devapro.features.setlock.model.SetLockPasswordScreenEvent
import io.github.devapro.features.setlock.model.SetLockPasswordScreenState

class OnToggleConfirmPasswordVisibilityReducer
    : Reducer<SetLockPasswordScreenAction.OnToggleConfirmPasswordVisibility, SetLockPasswordScreenState, SetLockPasswordScreenAction, SetLockPasswordScreenEvent> {

    override val actionClass = SetLockPasswordScreenAction.OnToggleConfirmPasswordVisibility::class

    override suspend fun reduce(
        action: SetLockPasswordScreenAction.OnToggleConfirmPasswordVisibility,
        getState: () -> SetLockPasswordScreenState
    ): Reducer.Result<SetLockPasswordScreenState, SetLockPasswordScreenAction.OnToggleConfirmPasswordVisibility, SetLockPasswordScreenEvent?> {
        val currentState = getState()

        return if (currentState is SetLockPasswordScreenState.Success) {
            Reducer.Result(
                state = currentState.copy(isConfirmPasswordVisible = !currentState.isConfirmPasswordVisible),
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