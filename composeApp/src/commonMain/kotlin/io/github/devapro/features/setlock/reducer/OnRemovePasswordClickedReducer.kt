package io.github.devapro.features.setlock.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.features.setlock.model.SetLockPasswordScreenAction
import io.github.devapro.features.setlock.model.SetLockPasswordScreenEvent
import io.github.devapro.features.setlock.model.SetLockPasswordScreenState

class OnRemovePasswordClickedReducer
    : Reducer<SetLockPasswordScreenAction.OnRemovePasswordClicked, SetLockPasswordScreenState, SetLockPasswordScreenAction, SetLockPasswordScreenEvent> {

    override val actionClass = SetLockPasswordScreenAction.OnRemovePasswordClicked::class

    override suspend fun reduce(
        action: SetLockPasswordScreenAction.OnRemovePasswordClicked,
        getState: () -> SetLockPasswordScreenState
    ): Reducer.Result<SetLockPasswordScreenState, SetLockPasswordScreenAction.OnRemovePasswordClicked, SetLockPasswordScreenEvent?> {
        val currentState = getState()

        return if (currentState is SetLockPasswordScreenState.Success && currentState.isVaultExists && !currentState.isProcessing) {
            Reducer.Result(
                state = currentState.copy(isProcessing = true),
                action = null,
                event = SetLockPasswordScreenEvent.RemovePassword
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