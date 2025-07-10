package io.github.devapro.features.importexport.reducer

import io.github.devapro.core.mvi.AppResult
import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.importexport.model.ImportExportScreenAction
import io.github.devapro.features.importexport.model.ImportExportScreenEvent
import io.github.devapro.features.importexport.model.ImportExportScreenState

class OnExportStartedReducer
    :
    Reducer<ImportExportScreenAction.OnExportStarted, ImportExportScreenState, ImportExportScreenAction, ImportExportScreenEvent> {

    override val actionClass = ImportExportScreenAction.OnExportStarted::class

    override suspend fun reduce(
        action: ImportExportScreenAction.OnExportStarted,
        getState: () -> ImportExportScreenState
    ): Reducer.Result<ImportExportScreenState, ImportExportScreenAction.OnExportStarted, ImportExportScreenEvent?> {
        val currentState = getState()
        return if (currentState is ImportExportScreenState.Loaded) {
            // TODO: Implement the export logic here
            val exportResult: AppResult<Unit> =
                AppResult.Success(Unit) // Replace with actual export logic
            when (exportResult) {
                is AppResult.Success -> {
                    Reducer.Result(
                        state = currentState.copy(isProcessing = false),
                        action = null,
                        event = ImportExportScreenEvent.ExportFile(currentState.selectedFormat)
                    )
                }

                is AppResult.Failure -> {
                    Reducer.Result(
                        state = currentState.copy(isProcessing = false),
                        action = null,
                        event = ImportExportScreenEvent.ShowError("Failed to export the vault.")
                    )
                }
            }
        } else {
            Reducer.Result(
                state = currentState,
                action = null,
                event = null
            )
        }
    }
} 