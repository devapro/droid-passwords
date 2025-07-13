package io.github.devapro.features.importexport.reducer

import io.github.devapro.core.mvi.AppResult
import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.importexport.model.FileFormat
import io.github.devapro.features.importexport.model.ImportExportScreenAction
import io.github.devapro.features.importexport.model.ImportExportScreenEvent
import io.github.devapro.features.importexport.model.ImportExportScreenState
import io.github.devapro.features.importexport.usecase.ImportFromCSVUseCase
import io.github.devapro.features.importexport.usecase.ImportFromDataUseCase
import io.github.devapro.features.importexport.usecase.ImportFromJsonUseCase

class OnImportFileSelectedReducer(
    private val importFromDataFile: ImportFromDataUseCase,
    private val importFromJsonFile: ImportFromJsonUseCase,
    private val importFromCsvFile: ImportFromCSVUseCase,
) : Reducer<ImportExportScreenAction.ImportFileSelected, ImportExportScreenState, ImportExportScreenAction, ImportExportScreenEvent> {

    override val actionClass = ImportExportScreenAction.ImportFileSelected::class

    override suspend fun reduce(
        action: ImportExportScreenAction.ImportFileSelected,
        getState: () -> ImportExportScreenState
    ): Reducer.Result<ImportExportScreenState, ImportExportScreenAction, ImportExportScreenEvent?> {
        val currentState = getState()
        return if (currentState is ImportExportScreenState.Loaded) {


            val result = when (currentState.selectedFormat) {
                FileFormat.CSV -> {
                    importFromCsvFile.execute(
                        file = action.file,
                        password = action.password
                    )
                }

                FileFormat.JSON -> {
                    importFromJsonFile.execute(
                        file = action.file,
                        password = action.password
                    )
                }

                FileFormat.DATA -> {
                    importFromDataFile.execute(
                        file = action.file,
                        password = action.password
                    )
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