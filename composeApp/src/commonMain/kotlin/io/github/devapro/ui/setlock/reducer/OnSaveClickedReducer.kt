package io.github.devapro.ui.setlock.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.setlock.model.SetLockPasswordScreenAction
import io.github.devapro.ui.setlock.model.SetLockPasswordScreenEvent
import io.github.devapro.ui.setlock.model.SetLockPasswordScreenState

class OnSaveClickedReducer
    : Reducer<SetLockPasswordScreenAction.OnSaveClicked, SetLockPasswordScreenState, SetLockPasswordScreenAction, SetLockPasswordScreenEvent> {

    override val actionClass = SetLockPasswordScreenAction.OnSaveClicked::class

    override suspend fun reduce(
        action: SetLockPasswordScreenAction.OnSaveClicked,
        getState: () -> SetLockPasswordScreenState
    ): Reducer.Result<SetLockPasswordScreenState, SetLockPasswordScreenAction.OnSaveClicked, SetLockPasswordScreenEvent?> {
        val currentState = getState()

        return if (currentState is SetLockPasswordScreenState.Success && currentState.isValid && !currentState.isProcessing) {
            Reducer.Result(
                state = currentState.copy(isProcessing = true),
                action = null,
                event = SetLockPasswordScreenEvent.SavePassword(
                    oldPassword = if (currentState.hasExistingPassword) currentState.currentPassword else null,
                    newPassword = currentState.newPassword
                )
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