package io.github.devapro.droid.itemdetails.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.itemdetails.model.PasswordDetailScreenAction
import io.github.devapro.droid.itemdetails.model.PasswordDetailScreenEvent
import io.github.devapro.droid.itemdetails.model.PasswordDetailScreenState

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
                state = currentState.copy(showDeleteConfirmation = true),
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