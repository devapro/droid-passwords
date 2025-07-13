package io.github.devapro.features.importexport.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.importexport.model.ImportExportScreenAction
import io.github.devapro.features.importexport.model.ImportExportScreenEvent
import io.github.devapro.features.importexport.model.ImportExportScreenState

class OnImportFileCancelledReducer
    :
    Reducer<ImportExportScreenAction.ImportFileCancelled, ImportExportScreenState, ImportExportScreenAction, ImportExportScreenEvent> {

    override val actionClass = ImportExportScreenAction.ImportFileCancelled::class

    override suspend fun reduce(
        action: ImportExportScreenAction.ImportFileCancelled,
        getState: () -> ImportExportScreenState
    ): Reducer.Result<ImportExportScreenState, ImportExportScreenAction, ImportExportScreenEvent?> {
        return Reducer.Result(
            state = getState(),
            action = null,
            event = ImportExportScreenEvent.ShowError("File selection cancelled")
        )
    }
} 