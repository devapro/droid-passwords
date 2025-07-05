package io.github.devapro.features.importexport.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.importexport.model.ImportExportScreenAction
import io.github.devapro.features.importexport.model.ImportExportScreenEvent
import io.github.devapro.features.importexport.model.ImportExportScreenState
import io.github.devapro.features.importexport.model.ImportExportTab

class OnSwitchToExportReducer
    : Reducer<ImportExportScreenAction.OnSwitchToExport, ImportExportScreenState, ImportExportScreenAction, ImportExportScreenEvent> {

    override val actionClass = ImportExportScreenAction.OnSwitchToExport::class

    override suspend fun reduce(
        action: ImportExportScreenAction.OnSwitchToExport,
        getState: () -> ImportExportScreenState
    ): Reducer.Result<ImportExportScreenState, ImportExportScreenAction.OnSwitchToExport, ImportExportScreenEvent?> {
        val currentState = getState()
        return if (currentState is ImportExportScreenState.Loaded) {
            Reducer.Result(
                state = currentState.copy(selectedTab = ImportExportTab.EXPORT),
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