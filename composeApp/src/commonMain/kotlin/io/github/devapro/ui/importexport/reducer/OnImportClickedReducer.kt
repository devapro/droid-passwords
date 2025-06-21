package io.github.devapro.ui.importexport.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.importexport.model.ImportExportScreenAction
import io.github.devapro.ui.importexport.model.ImportExportScreenEvent
import io.github.devapro.ui.importexport.model.ImportExportScreenState

class OnImportClickedReducer
    : Reducer<ImportExportScreenAction.OnImportClicked, ImportExportScreenState, ImportExportScreenAction, ImportExportScreenEvent> {

    override val actionClass = ImportExportScreenAction.OnImportClicked::class

    override suspend fun reduce(
        action: ImportExportScreenAction.OnImportClicked,
        getState: () -> ImportExportScreenState
    ): Reducer.Result<ImportExportScreenState, ImportExportScreenAction.OnImportClicked, ImportExportScreenEvent?> {
        val currentState = getState()
        return if (currentState is ImportExportScreenState.Success) {
            Reducer.Result(
                state = currentState.copy(isProcessing = true),
                action = null,
                event = ImportExportScreenEvent.ImportFile(currentState.selectedFormat)
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