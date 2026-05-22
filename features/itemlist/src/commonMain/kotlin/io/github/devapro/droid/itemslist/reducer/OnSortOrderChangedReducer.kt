package io.github.devapro.droid.itemslist.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.itemslist.applyFilter
import io.github.devapro.droid.itemslist.applySort
import io.github.devapro.droid.itemslist.model.PasswordListScreenAction
import io.github.devapro.droid.itemslist.model.PasswordListScreenEvent
import io.github.devapro.droid.itemslist.model.PasswordListScreenState

class OnSortOrderChangedReducer
    : Reducer<PasswordListScreenAction.OnSortOrderChanged, PasswordListScreenState, PasswordListScreenAction, PasswordListScreenEvent> {

    override val actionClass = PasswordListScreenAction.OnSortOrderChanged::class

    override suspend fun reduce(
        action: PasswordListScreenAction.OnSortOrderChanged,
        getState: () -> PasswordListScreenState
    ): Reducer.Result<PasswordListScreenState, PasswordListScreenAction.OnSortOrderChanged, PasswordListScreenEvent?> {
        val currentState = getState()
        return if (currentState is PasswordListScreenState.Success) {
            val sortedAll = currentState.passwords.applySort(action.order)
            val filtered = sortedAll
                .applyFilter(currentState.searchQuery)
                .applySort(action.order)
            Reducer.Result(
                state = currentState.copy(
                    sortOrder = action.order,
                    passwords = sortedAll,
                    filteredPasswords = filtered
                )
            )
        } else {
            Reducer.Result(state = currentState)
        }
    }
}
