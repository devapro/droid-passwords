package io.github.devapro.droid.importdata.reducer

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.importdata.model.ImportScreenAction
import io.github.devapro.droid.importdata.model.ImportScreenEvent
import io.github.devapro.droid.importdata.model.ImportScreenState
import io.github.devapro.droid.importdata.usecase.ComputeImportConflictsUseCase
import io.github.devapro.droid.importdata.usecase.ParseImportFileUseCase

class OnConfirmImportPasswordReducer(
    private val parseImportFile: ParseImportFileUseCase,
    private val computeConflicts: ComputeImportConflictsUseCase,
) : Reducer<ImportScreenAction.OnConfirmImportPassword, ImportScreenState, ImportScreenAction, ImportScreenEvent> {

    override val actionClass = ImportScreenAction.OnConfirmImportPassword::class

    override suspend fun reduce(
        action: ImportScreenAction.OnConfirmImportPassword,
        getState: () -> ImportScreenState
    ): Reducer.Result<ImportScreenState, ImportScreenAction, ImportScreenEvent?> {
        val current = getState()
        if (current !is ImportScreenState.Loaded) {
            return Reducer.Result(state = current, action = null, event = null)
        }

        val file = current.pendingFile
            ?: return Reducer.Result(state = current, action = null, event = null)

        if (current.password.isEmpty()) {
            return Reducer.Result(
                state = current.copy(passwordError = "Password is required"),
                action = null,
                event = null,
            )
        }

        return when (val parsed = parseImportFile.execute(file, current.selectedFormat, current.password)) {
            is AppResult.Success -> {
                val report = if (current.canMergeIntoActive) {
                    computeConflicts.execute(parsed.value.items)
                } else null
                Reducer.Result(
                    state = current.copy(
                        isProcessing = false,
                        parsedItems = parsed.value.items,
                        conflictReport = report,
                        newVaultName = parsed.value.embeddedVaultName ?: current.newVaultName,
                        // Keep the password: a DATA + NEW_VAULT import encrypts the new vault with it.
                        // It is cleared in OnConfirmImportReducer.resetState once the flow completes.
                        passwordError = null,
                        isPasswordVisible = false,
                        isConfirmDialogVisible = true,
                    ),
                    action = null,
                    event = null,
                )
            }

            is AppResult.Failure -> Reducer.Result(
                state = current.copy(
                    isProcessing = false,
                    passwordError = parsed.error.message ?: "Failed to read vault.",
                ),
                action = null,
                event = null,
            )
        }
    }
}
