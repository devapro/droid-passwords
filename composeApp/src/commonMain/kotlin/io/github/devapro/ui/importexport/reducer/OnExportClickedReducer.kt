package io.github.devapro.ui.importexport.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.importexport.model.ImportExportScreenAction
import io.github.devapro.ui.importexport.model.ImportExportScreenEvent
import io.github.devapro.ui.importexport.model.ImportExportScreenState

class OnExportClickedReducer
    : Reducer<ImportExportScreenAction.OnExportClicked, ImportExportScreenState, ImportExportScreenAction, ImportExportScreenEvent> {

    override val actionClass = ImportExportScreenAction.OnExportClicked::class

    override suspend fun reduce(
        action: ImportExportScreenAction.OnExportClicked,
        getState: () -> ImportExportScreenState
    ): Reducer.Result<ImportExportScreenState, ImportExportScreenAction.OnExportClicked, ImportExportScreenEvent?> {
        val currentState = getState()
        return if (currentState is ImportExportScreenState.Loaded) {
            Reducer.Result(
                state = currentState.copy(isProcessing = true),
                action = null,
                event = ImportExportScreenEvent.ExportFile(currentState.selectedFormat)
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