package io.github.devapro.droid.importdata.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.importdata.model.ImportScreenAction
import io.github.devapro.droid.importdata.model.ImportScreenEvent
import io.github.devapro.droid.importdata.model.ImportScreenState

class OnDismissPasswordDialogReducer
    :
    Reducer<ImportScreenAction.OnDismissPasswordDialog, ImportScreenState, ImportScreenAction, ImportScreenEvent> {

    override val actionClass = ImportScreenAction.OnDismissPasswordDialog::class

    override suspend fun reduce(
        action: ImportScreenAction.OnDismissPasswordDialog,
        getState: () -> ImportScreenState
    ): Reducer.Result<ImportScreenState, ImportScreenAction, ImportScreenEvent?> {
        val currentState = getState()
        return if (currentState is ImportScreenState.Loaded) {
            Reducer.Result(
                state = currentState.copy(
                    pendingFile = null,
                    password = "",
                    passwordError = null,
                    isPasswordVisible = false,
                    isProcessing = false
                ),
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
