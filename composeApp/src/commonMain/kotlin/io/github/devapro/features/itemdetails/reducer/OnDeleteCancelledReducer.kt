package io.github.devapro.features.itemdetails.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenAction
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenEvent
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenState

class OnDeleteCancelledReducer
    :
    Reducer<PasswordDetailScreenAction.OnDeleteCancelled, PasswordDetailScreenState, PasswordDetailScreenAction, PasswordDetailScreenEvent> {

    override val actionClass = PasswordDetailScreenAction.OnDeleteCancelled::class

    override suspend fun reduce(
        action: PasswordDetailScreenAction.OnDeleteCancelled,
        getState: () -> PasswordDetailScreenState
    ): Reducer.Result<PasswordDetailScreenState, PasswordDetailScreenAction.OnDeleteCancelled, PasswordDetailScreenEvent?> {
        val currentState = getState()

        return if (currentState is PasswordDetailScreenState.Success) {
            Reducer.Result(
                state = currentState.copy(showDeleteConfirmation = false),
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