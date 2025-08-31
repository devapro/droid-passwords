package io.github.devapro.droid.setlock.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.setlock.model.SetLockPasswordScreenAction
import io.github.devapro.droid.setlock.model.SetLockPasswordScreenEvent
import io.github.devapro.droid.setlock.model.SetLockPasswordScreenState

class OnToggleCurrentPasswordVisibilityReducer
    : Reducer<SetLockPasswordScreenAction.OnToggleCurrentPasswordVisibility, SetLockPasswordScreenState, SetLockPasswordScreenAction, SetLockPasswordScreenEvent> {

    override val actionClass = SetLockPasswordScreenAction.OnToggleCurrentPasswordVisibility::class

    override suspend fun reduce(
        action: SetLockPasswordScreenAction.OnToggleCurrentPasswordVisibility,
        getState: () -> SetLockPasswordScreenState
    ): Reducer.Result<SetLockPasswordScreenState, SetLockPasswordScreenAction.OnToggleCurrentPasswordVisibility, SetLockPasswordScreenEvent?> {
        val currentState = getState()

        return if (currentState is SetLockPasswordScreenState.Success) {
            Reducer.Result(
                state = currentState.copy(isCurrentPasswordVisible = !currentState.isCurrentPasswordVisible),
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