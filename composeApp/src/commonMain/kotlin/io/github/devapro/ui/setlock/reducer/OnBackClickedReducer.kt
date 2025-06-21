package io.github.devapro.ui.setlock.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.setlock.model.SetLockPasswordScreenAction
import io.github.devapro.ui.setlock.model.SetLockPasswordScreenEvent
import io.github.devapro.ui.setlock.model.SetLockPasswordScreenState

class OnBackClickedReducer
    : Reducer<SetLockPasswordScreenAction.OnBackClicked, SetLockPasswordScreenState, SetLockPasswordScreenAction, SetLockPasswordScreenEvent> {

    override val actionClass = SetLockPasswordScreenAction.OnBackClicked::class

    override suspend fun reduce(
        action: SetLockPasswordScreenAction.OnBackClicked,
        getState: () -> SetLockPasswordScreenState
    ): Reducer.Result<SetLockPasswordScreenState, SetLockPasswordScreenAction.OnBackClicked, SetLockPasswordScreenEvent?> {
        return Reducer.Result(
            state = getState(),
            action = null,
            event = SetLockPasswordScreenEvent.NavigateBack
        )
    }
} 