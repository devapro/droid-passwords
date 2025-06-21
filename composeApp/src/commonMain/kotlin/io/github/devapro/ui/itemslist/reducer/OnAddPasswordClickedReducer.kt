package io.github.devapro.ui.itemslist.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.itemslist.model.PasswordListScreenAction
import io.github.devapro.ui.itemslist.model.PasswordListScreenEvent
import io.github.devapro.ui.itemslist.model.PasswordListScreenState

class OnAddPasswordClickedReducer
    : Reducer<PasswordListScreenAction.OnAddPasswordClicked, PasswordListScreenState, PasswordListScreenAction, PasswordListScreenEvent> {

    override val actionClass = PasswordListScreenAction.OnAddPasswordClicked::class

    override suspend fun reduce(
        action: PasswordListScreenAction.OnAddPasswordClicked,
        getState: () -> PasswordListScreenState
    ): Reducer.Result<PasswordListScreenState, PasswordListScreenAction.OnAddPasswordClicked, PasswordListScreenEvent?> {
        return Reducer.Result(
            state = getState(),
            action = null,
            event = PasswordListScreenEvent.NavigateToAddPassword
        )
    }
} 