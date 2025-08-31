package io.github.devapro.droid.itemdetails.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.itemdetails.model.PasswordDetailScreenAction
import io.github.devapro.droid.itemdetails.model.PasswordDetailScreenEvent
import io.github.devapro.droid.itemdetails.model.PasswordDetailScreenState

class OnBackClickedReducer
    : Reducer<PasswordDetailScreenAction.OnBackClicked, PasswordDetailScreenState, PasswordDetailScreenAction, PasswordDetailScreenEvent> {

    override val actionClass = PasswordDetailScreenAction.OnBackClicked::class

    override suspend fun reduce(
        action: PasswordDetailScreenAction.OnBackClicked,
        getState: () -> PasswordDetailScreenState
    ): Reducer.Result<PasswordDetailScreenState, PasswordDetailScreenAction.OnBackClicked, PasswordDetailScreenEvent?> {
        return Reducer.Result(
            state = getState(),
            action = null,
            event = PasswordDetailScreenEvent.NavigateBack
        )
    }
} 