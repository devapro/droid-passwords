package io.github.devapro.features.itemslist.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.features.itemslist.model.PasswordListScreenAction
import io.github.devapro.features.itemslist.model.PasswordListScreenEvent
import io.github.devapro.features.itemslist.model.PasswordListScreenState

class OnPasswordItemClickedReducer
    : Reducer<PasswordListScreenAction.OnPasswordItemClicked, PasswordListScreenState, PasswordListScreenAction, PasswordListScreenEvent> {

    override val actionClass = PasswordListScreenAction.OnPasswordItemClicked::class

    override suspend fun reduce(
        action: PasswordListScreenAction.OnPasswordItemClicked,
        getState: () -> PasswordListScreenState
    ): Reducer.Result<PasswordListScreenState, PasswordListScreenAction.OnPasswordItemClicked, PasswordListScreenEvent?> {
        return Reducer.Result(
            state = getState(),
            action = null,
            event = PasswordListScreenEvent.NavigateToPasswordDetail(action.item)
        )
    }
} 