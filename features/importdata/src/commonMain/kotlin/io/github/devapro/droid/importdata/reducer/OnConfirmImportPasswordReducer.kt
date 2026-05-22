package io.github.devapro.droid.importdata.reducer

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.importdata.model.FileFormat
import io.github.devapro.droid.importdata.model.ImportScreenAction
import io.github.devapro.droid.importdata.model.ImportScreenEvent
import io.github.devapro.droid.importdata.model.ImportScreenState
import io.github.devapro.droid.importdata.usecase.ImportFromCSVUseCase
import io.github.devapro.droid.importdata.usecase.ImportFromDataUseCase
import io.github.devapro.droid.importdata.usecase.ImportFromJsonUseCase

class OnConfirmImportPasswordReducer(
    private val importFromDataFile: ImportFromDataUseCase,
    private val importFromJsonFile: ImportFromJsonUseCase,
    private val importFromCsvFile: ImportFromCSVUseCase,
) : Reducer<ImportScreenAction.OnConfirmImportPassword, ImportScreenState, ImportScreenAction, ImportScreenEvent> {

    override val actionClass = ImportScreenAction.OnConfirmImportPassword::class

    override suspend fun reduce(
        action: ImportScreenAction.OnConfirmImportPassword,
        getState: () -> ImportScreenState
    ): Reducer.Result<ImportScreenState, ImportScreenAction, ImportScreenEvent?> {
        val currentState = getState()
        if (currentState !is ImportScreenState.Loaded) {
            return Reducer.Result(state = currentState, action = null, event = null)
        }

        val file = currentState.pendingFile
        if (file == null) {
            return Reducer.Result(state = currentState, action = null, event = null)
        }

        if (currentState.password.isEmpty()) {
            return Reducer.Result(
                state = currentState.copy(passwordError = "Password is required"),
                action = null,
                event = null
            )
        }

        val result = when (currentState.selectedFormat) {
            FileFormat.CSV -> importFromCsvFile.execute(file, currentState.password)
            FileFormat.JSON -> importFromJsonFile.execute(file, currentState.password)
            FileFormat.DATA -> importFromDataFile.execute(file, currentState.password)
        }

        return when (result) {
            is AppResult.Success -> Reducer.Result(
                state = currentState.copy(
                    isProcessing = false,
                    pendingFile = null,
                    password = "",
                    passwordError = null,
                    isPasswordVisible = false
                ),
                action = null,
                event = ImportScreenEvent.ShowSuccess
            )

            is AppResult.Failure -> Reducer.Result(
                state = currentState.copy(
                    isProcessing = false,
                    passwordError = result.error.message ?: "Failed to import vault"
                ),
                action = null,
                event = null
            )
        }
    }
}
