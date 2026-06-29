package io.github.devapro.droid.importdata.reducer

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.importdata.model.FileFormat
import io.github.devapro.droid.importdata.model.ImportScreenAction
import io.github.devapro.droid.importdata.model.ImportScreenEvent
import io.github.devapro.droid.importdata.model.ImportScreenState
import io.github.devapro.droid.importdata.usecase.ComputeImportConflictsUseCase
import io.github.devapro.droid.importdata.usecase.ParseImportFileUseCase

class OnImportFileSelectedReducer(
    private val parseImportFile: ParseImportFileUseCase,
    private val computeConflicts: ComputeImportConflictsUseCase,
) : Reducer<ImportScreenAction.ImportFileSelected, ImportScreenState, ImportScreenAction, ImportScreenEvent> {

    override val actionClass = ImportScreenAction.ImportFileSelected::class

    override suspend fun reduce(
        action: ImportScreenAction.ImportFileSelected,
        getState: () -> ImportScreenState
    ): Reducer.Result<ImportScreenState, ImportScreenAction, ImportScreenEvent?> {
        val current = getState()
        if (current !is ImportScreenState.Loaded) {
            return Reducer.Result(state = current, action = null, event = null)
        }

        // DATA needs decryption — keep showing the password dialog flow.
        if (current.selectedFormat == FileFormat.DATA) {
            return Reducer.Result(
                state = current.copy(
                    isProcessing = false,
                    pendingFile = action.file,
                    password = "",
                    passwordError = null,
                    isPasswordVisible = false,
                ),
                action = null,
                event = null,
            )
        }

        // CSV/JSON: parse immediately, then show the confirmation dialog.
        return when (val parsed = parseImportFile.execute(action.file, current.selectedFormat, password = null)) {
            is AppResult.Success -> {
                val report = if (current.canMergeIntoActive) {
                    computeConflicts.execute(parsed.value.items)
                } else null
                Reducer.Result(
                    state = current.copy(
                        isProcessing = false,
                        pendingFile = action.file,
                        parsedItems = parsed.value.items,
                        conflictReport = report,
                        newVaultName = parsed.value.embeddedVaultName ?: current.newVaultName,
                        isConfirmDialogVisible = true,
                    ),
                    action = null,
                    event = null,
                )
            }

            is AppResult.Failure -> Reducer.Result(
                state = current.copy(isProcessing = false, pendingFile = null),
                action = null,
                event = ImportScreenEvent.ShowError(parsed.error.message ?: "Failed to read file."),
            )
        }
    }
}
