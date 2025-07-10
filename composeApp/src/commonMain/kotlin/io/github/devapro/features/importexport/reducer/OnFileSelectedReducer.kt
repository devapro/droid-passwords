package io.github.devapro.features.importexport.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.importexport.model.ImportExportScreenAction
import io.github.devapro.features.importexport.model.ImportExportScreenEvent
import io.github.devapro.features.importexport.model.ImportExportScreenState

class OnFileSelectedReducer
    :
    Reducer<ImportExportScreenAction.OnFileSelected, ImportExportScreenState, ImportExportScreenAction, ImportExportScreenEvent> {

    override val actionClass = ImportExportScreenAction.OnFileSelected::class

    override suspend fun reduce(
        action: ImportExportScreenAction.OnFileSelected,
        getState: () -> ImportExportScreenState
    ): Reducer.Result<ImportExportScreenState, ImportExportScreenAction, ImportExportScreenEvent?> {
        val currentState = getState()
        return if (currentState is ImportExportScreenState.Loaded) {
            Reducer.Result(
                state = currentState.copy(isProcessing = true),
                action = ImportExportScreenAction.OnExportStarted,
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