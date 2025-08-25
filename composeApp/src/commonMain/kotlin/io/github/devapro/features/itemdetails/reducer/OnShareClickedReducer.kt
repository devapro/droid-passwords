package io.github.devapro.features.itemdetails.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenAction
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenEvent
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenState

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