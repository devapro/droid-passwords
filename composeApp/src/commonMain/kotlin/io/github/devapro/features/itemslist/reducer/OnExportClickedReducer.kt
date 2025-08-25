package io.github.devapro.features.itemslist.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.features.itemslist.model.PasswordListScreenAction
import io.github.devapro.features.itemslist.model.PasswordListScreenEvent
import io.github.devapro.features.itemslist.model.PasswordListScreenState

class OnExportClickedReducer
    :
    Reducer<PasswordListScreenAction.OnExportClicked, PasswordListScreenState, PasswordListScreenAction, PasswordListScreenEvent> {

    override val actionClass = PasswordListScreenAction.OnExportClicked::class

    override suspend fun reduce(
        action: PasswordListScreenAction.OnExportClicked,
        getState: () -> PasswordListScreenState
    ): Reducer.Result<PasswordListScreenState, PasswordListScreenAction.OnExportClicked, PasswordListScreenEvent?> {
        return Reducer.Result(
            state = getState(),
            action = null,
            event = PasswordListScreenEvent.NavigateToExport
        )
    }
} 