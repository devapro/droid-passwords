package io.github.devapro.features.itemslist.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.itemslist.model.PasswordListScreenAction
import io.github.devapro.features.itemslist.model.PasswordListScreenEvent
import io.github.devapro.features.itemslist.model.PasswordListScreenState

class OnRefreshReducer
    : Reducer<PasswordListScreenAction.OnRefresh, PasswordListScreenState, PasswordListScreenAction, PasswordListScreenEvent> {

    override val actionClass = PasswordListScreenAction.OnRefresh::class

    override suspend fun reduce(
        action: PasswordListScreenAction.OnRefresh,
        getState: () -> PasswordListScreenState
    ): Reducer.Result<PasswordListScreenState, PasswordListScreenAction.OnRefresh, PasswordListScreenEvent?> {
        val currentState = getState()

        return if (currentState is PasswordListScreenState.Success) {
            Reducer.Result(
                state = currentState.copy(isRefreshing = true),
                action = null,
                event = PasswordListScreenEvent.RefreshPasswordList
            )
        } else {
            Reducer.Result(
                state = currentState,
                action = null,
                event = PasswordListScreenEvent.RefreshPasswordList
            )
        }
    }
} 