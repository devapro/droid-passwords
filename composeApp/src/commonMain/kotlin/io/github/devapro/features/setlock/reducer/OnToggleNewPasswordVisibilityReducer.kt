package io.github.devapro.features.setlock.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.features.setlock.model.SetLockPasswordScreenAction
import io.github.devapro.features.setlock.model.SetLockPasswordScreenEvent
import io.github.devapro.features.setlock.model.SetLockPasswordScreenState

class OnToggleNewPasswordVisibilityReducer
    : Reducer<SetLockPasswordScreenAction.OnToggleNewPasswordVisibility, SetLockPasswordScreenState, SetLockPasswordScreenAction, SetLockPasswordScreenEvent> {

    override val actionClass = SetLockPasswordScreenAction.OnToggleNewPasswordVisibility::class

    override suspend fun reduce(
        action: SetLockPasswordScreenAction.OnToggleNewPasswordVisibility,
        getState: () -> SetLockPasswordScreenState
    ): Reducer.Result<SetLockPasswordScreenState, SetLockPasswordScreenAction.OnToggleNewPasswordVisibility, SetLockPasswordScreenEvent?> {
        val currentState = getState()

        return if (currentState is SetLockPasswordScreenState.Success) {
            Reducer.Result(
                state = currentState.copy(isNewPasswordVisible = !currentState.isNewPasswordVisible),
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