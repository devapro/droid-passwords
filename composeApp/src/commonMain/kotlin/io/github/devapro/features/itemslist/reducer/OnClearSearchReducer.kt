package io.github.devapro.features.itemslist.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.itemslist.model.PasswordListScreenAction
import io.github.devapro.features.itemslist.model.PasswordListScreenEvent
import io.github.devapro.features.itemslist.model.PasswordListScreenState

class OnClearSearchReducer
    : Reducer<PasswordListScreenAction.OnClearSearch, PasswordListScreenState, PasswordListScreenAction, PasswordListScreenEvent> {

    override val actionClass = PasswordListScreenAction.OnClearSearch::class

    override suspend fun reduce(
        action: PasswordListScreenAction.OnClearSearch,
        getState: () -> PasswordListScreenState
    ): Reducer.Result<PasswordListScreenState, PasswordListScreenAction.OnClearSearch, PasswordListScreenEvent?> {
        val currentState = getState()

        return if (currentState is PasswordListScreenState.Success) {
            val newState = currentState.copy(
                searchQuery = "",
                filteredPasswords = currentState.passwords,
                hasSearchQuery = false
            )
            Reducer.Result(
                state = newState,
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