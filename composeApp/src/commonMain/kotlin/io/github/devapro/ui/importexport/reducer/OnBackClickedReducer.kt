package io.github.devapro.ui.importexport.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.importexport.model.ImportExportScreenAction
import io.github.devapro.ui.importexport.model.ImportExportScreenEvent
import io.github.devapro.ui.importexport.model.ImportExportScreenState

class OnBackClickedReducer
    : Reducer<ImportExportScreenAction.OnBackClicked, ImportExportScreenState, ImportExportScreenAction, ImportExportScreenEvent> {

    override val actionClass = ImportExportScreenAction.OnBackClicked::class

    override suspend fun reduce(
        action: ImportExportScreenAction.OnBackClicked,
        getState: () -> ImportExportScreenState
    ): Reducer.Result<ImportExportScreenState, ImportExportScreenAction.OnBackClicked, ImportExportScreenEvent?> {
        return Reducer.Result(
            state = getState(),
            action = null,
            event = ImportExportScreenEvent.NavigateBack
        )
    }
} 