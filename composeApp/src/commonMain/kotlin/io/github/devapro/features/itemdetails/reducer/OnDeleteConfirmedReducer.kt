package io.github.devapro.features.itemdetails.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenAction
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenEvent
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenState

class OnDeleteConfirmedReducer
    :
    Reducer<PasswordDetailScreenAction.OnDeleteConfirmed, PasswordDetailScreenState, PasswordDetailScreenAction, PasswordDetailScreenEvent> {

    override val actionClass = PasswordDetailScreenAction.OnDeleteConfirmed::class

    override suspend fun reduce(
        action: PasswordDetailScreenAction.OnDeleteConfirmed,
        getState: () -> PasswordDetailScreenState
    ): Reducer.Result<PasswordDetailScreenState, PasswordDetailScreenAction.OnDeleteConfirmed, PasswordDetailScreenEvent?> {
        val currentState = getState()

        return if (currentState is PasswordDetailScreenState.Success) {
            Reducer.Result(
                state = currentState.copy(
                    showDeleteConfirmation = false,
                    isLoading = true
                ),
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