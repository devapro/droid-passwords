package io.github.devapro.droid.importdata.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.importdata.model.ImportScreenAction
import io.github.devapro.droid.importdata.model.ImportScreenEvent
import io.github.devapro.droid.importdata.model.ImportScreenState

class OnDismissConfirmDialogReducer
    : Reducer<ImportScreenAction.OnDismissConfirmDialog, ImportScreenState, ImportScreenAction, ImportScreenEvent> {

    override val actionClass = ImportScreenAction.OnDismissConfirmDialog::class

    override suspend fun reduce(
        action: ImportScreenAction.OnDismissConfirmDialog,
        getState: () -> ImportScreenState
    ): Reducer.Result<ImportScreenState, ImportScreenAction.OnDismissConfirmDialog, ImportScreenEvent?> {
        val current = getState()
        return if (current is ImportScreenState.Loaded) {
            Reducer.Result(
                state = current.copy(
                    isConfirmDialogVisible = false,
                    parsedItems = null,
                    conflictReport = null,
                    pendingFile = null,
                    isProcessing = false,
                    password = "",
                    passwordError = null,
                    isPasswordVisible = false,
                ),
                action = null,
                event = null,
            )
        } else {
            Reducer.Result(state = current, action = null, event = null)
        }
    }
}
