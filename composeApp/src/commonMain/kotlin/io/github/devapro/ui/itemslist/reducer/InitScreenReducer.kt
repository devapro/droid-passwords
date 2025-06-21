package io.github.devapro.ui.itemslist.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.itemslist.model.PasswordListScreenAction
import io.github.devapro.ui.itemslist.model.PasswordListScreenEvent
import io.github.devapro.ui.itemslist.model.PasswordListScreenState

class InitScreenReducer
    : Reducer<PasswordListScreenAction.InitScreen, PasswordListScreenState, PasswordListScreenAction, PasswordListScreenEvent> {

    override val actionClass = PasswordListScreenAction.InitScreen::class

    override suspend fun reduce(
        action: PasswordListScreenAction.InitScreen,
        getState: () -> PasswordListScreenState
    ): Reducer.Result<PasswordListScreenState, PasswordListScreenAction.InitScreen, PasswordListScreenEvent?> {
        return Reducer.Result(
            state = PasswordListScreenState.Success(
                passwords = emptyList(),
                filteredPasswords = emptyList(),
                searchQuery = "",
                isLoading = false,
                isRefreshing = false,
                isEmpty = true,
                hasSearchQuery = false
            ),
            action = null,
            event = PasswordListScreenEvent.RefreshPasswordList
        )
    }
} 