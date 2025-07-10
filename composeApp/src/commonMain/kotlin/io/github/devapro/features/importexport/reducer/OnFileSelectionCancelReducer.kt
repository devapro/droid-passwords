package io.github.devapro.features.importexport.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.importexport.model.ImportExportScreenAction
import io.github.devapro.features.importexport.model.ImportExportScreenEvent
import io.github.devapro.features.importexport.model.ImportExportScreenState

class OnFileSelectionCancelReducer
    :
    Reducer<ImportExportScreenAction.ExportFileCancelled, ImportExportScreenState, ImportExportScreenAction, ImportExportScreenEvent> {

    override val actionClass = ImportExportScreenAction.ExportFileCancelled::class

    override suspend fun reduce(
        action: ImportExportScreenAction.ExportFileCancelled,
        getState: () -> ImportExportScreenState
    ): Reducer.Result<ImportExportScreenState, ImportExportScreenAction, ImportExportScreenEvent?> {
        return Reducer.Result(
            state = getState(),
            action = null,
            event = ImportExportScreenEvent.ShowError("File selection cancelled")
        )
    }
} 