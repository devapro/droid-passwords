package io.github.devapro.ui.itemslist.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.itemslist.model.PasswordListScreenAction
import io.github.devapro.ui.itemslist.model.PasswordListScreenEvent
import io.github.devapro.ui.itemslist.model.PasswordListScreenState

class OnImportExportClickedReducer
    : Reducer<PasswordListScreenAction.OnImportExportClicked, PasswordListScreenState, PasswordListScreenAction, PasswordListScreenEvent> {

    override val actionClass = PasswordListScreenAction.OnImportExportClicked::class

    override suspend fun reduce(
        action: PasswordListScreenAction.OnImportExportClicked,
        getState: () -> PasswordListScreenState
    ): Reducer.Result<PasswordListScreenState, PasswordListScreenAction.OnImportExportClicked, PasswordListScreenEvent?> {
        return Reducer.Result(
            state = getState(),
            action = null,
            event = PasswordListScreenEvent.NavigateToImportExport
        )
    }
} 