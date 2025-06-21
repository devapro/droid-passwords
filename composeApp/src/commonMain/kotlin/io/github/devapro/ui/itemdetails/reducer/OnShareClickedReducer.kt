package io.github.devapro.ui.itemdetails.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.itemdetails.model.PasswordDetailScreenAction
import io.github.devapro.ui.itemdetails.model.PasswordDetailScreenEvent
import io.github.devapro.ui.itemdetails.model.PasswordDetailScreenState

class OnShareClickedReducer
    : Reducer<PasswordDetailScreenAction.OnShareClicked, PasswordDetailScreenState, PasswordDetailScreenAction, PasswordDetailScreenEvent> {

    override val actionClass = PasswordDetailScreenAction.OnShareClicked::class

    override suspend fun reduce(
        action: PasswordDetailScreenAction.OnShareClicked,
        getState: () -> PasswordDetailScreenState
    ): Reducer.Result<PasswordDetailScreenState, PasswordDetailScreenAction.OnShareClicked, PasswordDetailScreenEvent?> {
        val currentState = getState()

        return if (currentState is PasswordDetailScreenState.Success) {
            Reducer.Result(
                state = currentState,
                action = null,
                event = PasswordDetailScreenEvent.ShareItem(currentState.item)
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