package io.github.devapro.features.setlock.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.setlock.model.SetLockPasswordScreenAction
import io.github.devapro.features.setlock.model.SetLockPasswordScreenEvent
import io.github.devapro.features.setlock.model.SetLockPasswordScreenState

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