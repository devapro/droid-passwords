package io.github.devapro.ui.itemslist.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.itemslist.model.PasswordListScreenAction
import io.github.devapro.ui.itemslist.model.PasswordListScreenEvent
import io.github.devapro.ui.itemslist.model.PasswordListScreenState

class OnDeletePasswordClickedReducer
    : Reducer<PasswordListScreenAction.OnDeletePasswordClicked, PasswordListScreenState, PasswordListScreenAction, PasswordListScreenEvent> {

    override val actionClass = PasswordListScreenAction.OnDeletePasswordClicked::class

    override suspend fun reduce(
        action: PasswordListScreenAction.OnDeletePasswordClicked,
        getState: () -> PasswordListScreenState
    ): Reducer.Result<PasswordListScreenState, PasswordListScreenAction.OnDeletePasswordClicked, PasswordListScreenEvent?> {
        return Reducer.Result(
            state = getState(),
            action = null,
            event = PasswordListScreenEvent.DeletePassword(action.item)
        )
    }
} 