package io.github.devapro.droid.itemslist.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.itemslist.model.PasswordListScreenAction
import io.github.devapro.droid.itemslist.model.PasswordListScreenEvent
import io.github.devapro.droid.itemslist.model.PasswordListScreenState

class OnBackClickedReducer
    :
    Reducer<PasswordListScreenAction.OnBackClicked, PasswordListScreenState, PasswordListScreenAction, PasswordListScreenEvent> {

    override val actionClass = PasswordListScreenAction.OnBackClicked::class

    override suspend fun reduce(
        action: PasswordListScreenAction.OnBackClicked,
        getState: () -> PasswordListScreenState
    ): Reducer.Result<PasswordListScreenState, PasswordListScreenAction.OnBackClicked, PasswordListScreenEvent?> {
        val currentState = getState()

        return if (currentState is PasswordListScreenState.Success) {
            Reducer.Result(
                state = currentState.copy(isRefreshing = true),
                action = null,
                event = PasswordListScreenEvent.OnBackClicked
            )
        } else {
            Reducer.Result(
                state = currentState,
                action = null,
                event = PasswordListScreenEvent.OnBackClicked
            )
        }
    }
} 