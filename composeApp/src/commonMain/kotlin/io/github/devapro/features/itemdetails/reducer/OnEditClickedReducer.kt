package io.github.devapro.features.itemdetails.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenAction
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenEvent
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenState

class OnEditClickedReducer
    : Reducer<PasswordDetailScreenAction.OnEditClicked, PasswordDetailScreenState, PasswordDetailScreenAction, PasswordDetailScreenEvent> {

    override val actionClass = PasswordDetailScreenAction.OnEditClicked::class

    override suspend fun reduce(
        action: PasswordDetailScreenAction.OnEditClicked,
        getState: () -> PasswordDetailScreenState
    ): Reducer.Result<PasswordDetailScreenState, PasswordDetailScreenAction.OnEditClicked, PasswordDetailScreenEvent?> {
        val currentState = getState()

        return if (currentState is PasswordDetailScreenState.Success) {
            Reducer.Result(
                state = currentState,
                action = null,
                event = PasswordDetailScreenEvent.NavigateToEdit(currentState.item)
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