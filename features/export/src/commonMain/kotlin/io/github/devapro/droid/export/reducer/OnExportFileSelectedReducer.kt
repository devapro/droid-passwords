package io.github.devapro.droid.export.reducer

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.export.model.ExportScreenAction
import io.github.devapro.droid.export.model.ExportScreenEvent
import io.github.devapro.droid.export.model.ExportScreenState
import io.github.devapro.droid.export.model.FileFormat
import io.github.devapro.droid.export.usecase.SaveCSVFileUseCase
import io.github.devapro.droid.export.usecase.SaveDataFileUseCase
import io.github.devapro.droid.export.usecase.SaveJsonFileUseCase

class OnExportFileSelectedReducer(
    private val saveDataFileUseCase: SaveDataFileUseCase,
    private val saveJsonFileUseCase: SaveJsonFileUseCase,
    private val saveCSVFileUseCase: SaveCSVFileUseCase,
) : Reducer<ExportScreenAction.ExportFileSelected, ExportScreenState, ExportScreenAction, ExportScreenEvent> {

    override val actionClass = ExportScreenAction.ExportFileSelected::class

    override suspend fun reduce(
        action: ExportScreenAction.ExportFileSelected,
        getState: () -> ExportScreenState
    ): Reducer.Result<ExportScreenState, ExportScreenAction, ExportScreenEvent?> {
        val currentState = getState()
        return if (currentState is ExportScreenState.Loaded) {


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
                        event = ExportScreenEvent.ShowSuccess
                    )
                }

                is AppResult.Failure -> {
                    Reducer.Result(
                        state = currentState.copy(isProcessing = false),
                        action = null,
                        event = ExportScreenEvent.ShowError("Failed to export vault: ${result.error.message}")
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