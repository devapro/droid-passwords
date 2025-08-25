package io.github.devapro.features.importdata.reducer

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.features.importdata.model.FileFormat
import io.github.devapro.features.importdata.model.ImportScreenAction
import io.github.devapro.features.importdata.model.ImportScreenEvent
import io.github.devapro.features.importdata.model.ImportScreenState
import io.github.devapro.features.importdata.usecase.ImportFromCSVUseCase
import io.github.devapro.features.importdata.usecase.ImportFromDataUseCase
import io.github.devapro.features.importdata.usecase.ImportFromJsonUseCase

class OnImportFileSelectedReducer(
    private val importFromDataFile: ImportFromDataUseCase,
    private val importFromJsonFile: ImportFromJsonUseCase,
    private val importFromCsvFile: ImportFromCSVUseCase,
) : Reducer<ImportScreenAction.ImportFileSelected, ImportScreenState, ImportScreenAction, ImportScreenEvent> {

    override val actionClass = ImportScreenAction.ImportFileSelected::class

    override suspend fun reduce(
        action: ImportScreenAction.ImportFileSelected,
        getState: () -> ImportScreenState
    ): Reducer.Result<ImportScreenState, ImportScreenAction, ImportScreenEvent?> {
        val currentState = getState()
        return if (currentState is ImportScreenState.Loaded) {

            val result = when (currentState.selectedFormat) {
                FileFormat.CSV -> {
                    importFromCsvFile.execute(
                        file = action.file,
                        password = currentState.password
                    )
                }

                FileFormat.JSON -> {
                    importFromJsonFile.execute(
                        file = action.file,
                        password = currentState.password
                    )
                }

                FileFormat.DATA -> {
                    importFromDataFile.execute(
                        file = action.file,
                        password = currentState.password
                    )
                }
            }

            when (result) {
                is AppResult.Success -> {
                    Reducer.Result(
                        state = currentState.copy(isProcessing = false),
                        action = null,
                        event = ImportScreenEvent.ShowSuccess
                    )
                }

                is AppResult.Failure -> {
                    Reducer.Result(
                        state = currentState.copy(isProcessing = false),
                        action = null,
                        event = ImportScreenEvent.ShowError("Failed to export vault: ${result.error.message}")
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