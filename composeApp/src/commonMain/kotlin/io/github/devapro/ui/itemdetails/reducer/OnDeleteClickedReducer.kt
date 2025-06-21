package io.github.devapro.ui.itemdetails.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.itemdetails.model.PasswordDetailScreenAction
import io.github.devapro.ui.itemdetails.model.PasswordDetailScreenEvent
import io.github.devapro.ui.itemdetails.model.PasswordDetailScreenState

class OnDeleteClickedReducer
    : Reducer<PasswordDetailScreenAction.OnDeleteClicked, PasswordDetailScreenState, PasswordDetailScreenAction, PasswordDetailScreenEvent> {

    override val actionClass = PasswordDetailScreenAction.OnDeleteClicked::class

    override suspend fun reduce(
        action: PasswordDetailScreenAction.OnDeleteClicked,
        getState: () -> PasswordDetailScreenState
    ): Reducer.Result<PasswordDetailScreenState, PasswordDetailScreenAction.OnDeleteClicked, PasswordDetailScreenEvent?> {
        val currentState = getState()

        return if (currentState is PasswordDetailScreenState.Success) {
            Reducer.Result(
                state = currentState.copy(isLoading = true),
                action = null,
                event = PasswordDetailScreenEvent.DeleteItem(currentState.item)
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