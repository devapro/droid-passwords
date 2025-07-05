package io.github.devapro.features.importexport.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.importexport.model.ImportExportScreenAction
import io.github.devapro.features.importexport.model.ImportExportScreenEvent
import io.github.devapro.features.importexport.model.ImportExportScreenState
import io.github.devapro.features.importexport.model.ImportExportTab

class OnSwitchToImportReducer
    : Reducer<ImportExportScreenAction.OnSwitchToImport, ImportExportScreenState, ImportExportScreenAction, ImportExportScreenEvent> {

    override val actionClass = ImportExportScreenAction.OnSwitchToImport::class

    override suspend fun reduce(
        action: ImportExportScreenAction.OnSwitchToImport,
        getState: () -> ImportExportScreenState
    ): Reducer.Result<ImportExportScreenState, ImportExportScreenAction.OnSwitchToImport, ImportExportScreenEvent?> {
        val currentState = getState()
        return if (currentState is ImportExportScreenState.Loaded) {
            Reducer.Result(
                state = currentState.copy(selectedTab = ImportExportTab.IMPORT),
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