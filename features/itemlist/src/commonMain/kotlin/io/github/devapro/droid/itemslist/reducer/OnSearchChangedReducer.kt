package io.github.devapro.droid.itemslist.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.itemslist.applyFilter
import io.github.devapro.droid.itemslist.applySort
import io.github.devapro.droid.itemslist.model.PasswordListScreenAction
import io.github.devapro.droid.itemslist.model.PasswordListScreenEvent
import io.github.devapro.droid.itemslist.model.PasswordListScreenState

class OnSearchChangedReducer
    : Reducer<PasswordListScreenAction.OnSearchChanged, PasswordListScreenState, PasswordListScreenAction, PasswordListScreenEvent> {

    override val actionClass = PasswordListScreenAction.OnSearchChanged::class

    override suspend fun reduce(
        action: PasswordListScreenAction.OnSearchChanged,
        getState: () -> PasswordListScreenState
    ): Reducer.Result<PasswordListScreenState, PasswordListScreenAction.OnSearchChanged, PasswordListScreenEvent?> {
        val currentState = getState()

        return if (currentState is PasswordListScreenState.Success) {
            val filteredPasswords = currentState.passwords
                .applyFilter(action.query)
                .applySort(currentState.sortOrder)

            val newState = currentState.copy(
                searchQuery = action.query,
                filteredPasswords = filteredPasswords,
                hasSearchQuery = action.query.isNotBlank()
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