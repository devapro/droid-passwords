package io.github.devapro.droid.importdata.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.importdata.model.ImportScreenAction
import io.github.devapro.droid.importdata.model.ImportScreenEvent
import io.github.devapro.droid.importdata.model.ImportScreenState

class OnImportFileSelectedReducer
    :
    Reducer<ImportScreenAction.ImportFileSelected, ImportScreenState, ImportScreenAction, ImportScreenEvent> {

    override val actionClass = ImportScreenAction.ImportFileSelected::class

    override suspend fun reduce(
        action: ImportScreenAction.ImportFileSelected,
        getState: () -> ImportScreenState
    ): Reducer.Result<ImportScreenState, ImportScreenAction, ImportScreenEvent?> {
        val currentState = getState()
        return if (currentState is ImportScreenState.Loaded) {
            Reducer.Result(
                state = currentState.copy(
                    isProcessing = false,
                    pendingFile = action.file,
                    password = "",
                    passwordError = null,
                    isPasswordVisible = false
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
