package io.github.devapro.features.importexport.reducer

import io.github.devapro.core.mvi.AppResult
import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.importexport.model.FileFormat
import io.github.devapro.features.importexport.model.ImportExportScreenAction
import io.github.devapro.features.importexport.model.ImportExportScreenEvent
import io.github.devapro.features.importexport.model.ImportExportScreenState
import io.github.devapro.features.importexport.usecase.SaveCSVFileUseCase
import io.github.devapro.features.importexport.usecase.SaveDataFileUseCase
import io.github.devapro.features.importexport.usecase.SaveJsonFileUseCase

class OnExportFileSelectedReducer(
    private val saveDataFileUseCase: SaveDataFileUseCase,
    private val saveJsonFileUseCase: SaveJsonFileUseCase,
    private val saveCSVFileUseCase: SaveCSVFileUseCase,
) : Reducer<ImportExportScreenAction.ExportFileSelected, ImportExportScreenState, ImportExportScreenAction, ImportExportScreenEvent> {

    override val actionClass = ImportExportScreenAction.ExportFileSelected::class

    override suspend fun reduce(
        action: ImportExportScreenAction.ExportFileSelected,
        getState: () -> ImportExportScreenState
    ): Reducer.Result<ImportExportScreenState, ImportExportScreenAction, ImportExportScreenEvent?> {
        val currentState = getState()
        return if (currentState is ImportExportScreenState.Loaded) {


            val result = when (currentState.selectedFormat) {
                FileFormat.CSV -> {
                    saveCSVFileUseCase.execute(action.file)
                }

                FileFormat.JSON -> {
                    saveJsonFileUseCase.execute(action.file)
                }

                FileFormat.DATA -> {
                    saveDataFileUseCase.execute(action.file)
                }
            }

            when (result) {
                is AppResult.Success -> {
                    Reducer.Result(
                        state = currentState.copy(isProcessing = false),
                        action = null,
                        event = ImportExportScreenEvent.ShowSuccess
                    )
                }

                is AppResult.Failure -> {
                    Reducer.Result(
                        state = currentState.copy(isProcessing = false),
                        action = null,
                        event = ImportExportScreenEvent.ShowError("Failed to export vault: ${result.error.message}")
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