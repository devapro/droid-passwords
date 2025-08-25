package io.github.devapro.features.itemslist.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.features.itemslist.model.PasswordListScreenAction
import io.github.devapro.features.itemslist.model.PasswordListScreenEvent
import io.github.devapro.features.itemslist.model.PasswordListScreenState

class OnSearchChangedReducer
    : Reducer<PasswordListScreenAction.OnSearchChanged, PasswordListScreenState, PasswordListScreenAction, PasswordListScreenEvent> {

    override val actionClass = PasswordListScreenAction.OnSearchChanged::class

    override suspend fun reduce(
        action: PasswordListScreenAction.OnSearchChanged,
        getState: () -> PasswordListScreenState
    ): Reducer.Result<PasswordListScreenState, PasswordListScreenAction.OnSearchChanged, PasswordListScreenEvent?> {
        val currentState = getState()

        return if (currentState is PasswordListScreenState.Success) {
            val filteredPasswords = if (action.query.isBlank()) {
                currentState.passwords
            } else {
                currentState.passwords.filter { password ->
                    password.title.contains(action.query, ignoreCase = true) ||
                    password.description.contains(action.query, ignoreCase = true) ||
                    password.url.contains(action.query, ignoreCase = true) ||
                    password.username.contains(action.query, ignoreCase = true)
                }
            }

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