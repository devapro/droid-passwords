package io.github.devapro.ui.itemdetails.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.itemdetails.model.PasswordDetailScreenAction
import io.github.devapro.ui.itemdetails.model.PasswordDetailScreenEvent
import io.github.devapro.ui.itemdetails.model.PasswordDetailScreenState

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