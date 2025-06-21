package io.github.devapro.ui.itemslist.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.itemslist.model.PasswordListScreenAction
import io.github.devapro.ui.itemslist.model.PasswordListScreenEvent
import io.github.devapro.ui.itemslist.model.PasswordListScreenState

class OnSettingsClickedReducer
    : Reducer<PasswordListScreenAction.OnSettingsClicked, PasswordListScreenState, PasswordListScreenAction, PasswordListScreenEvent> {

    override val actionClass = PasswordListScreenAction.OnSettingsClicked::class

    override suspend fun reduce(
        action: PasswordListScreenAction.OnSettingsClicked,
        getState: () -> PasswordListScreenState
    ): Reducer.Result<PasswordListScreenState, PasswordListScreenAction.OnSettingsClicked, PasswordListScreenEvent?> {
        return Reducer.Result(
            state = getState(),
            action = null,
            event = PasswordListScreenEvent.NavigateToSettings
        )
    }
} 