package io.github.devapro.droid.importdata.reducer

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.importdata.model.ImportScreenAction
import io.github.devapro.droid.importdata.model.ImportScreenEvent
import io.github.devapro.droid.importdata.model.ImportScreenState
import io.github.devapro.droid.importdata.model.ImportStrategy
import io.github.devapro.droid.importdata.model.ImportTarget
import io.github.devapro.droid.importdata.usecase.ApplyImportToActiveVaultUseCase
import io.github.devapro.droid.importdata.usecase.CreateVaultFromImportUseCase

class OnConfirmImportReducer(
    private val applyImport: ApplyImportToActiveVaultUseCase,
    private val createVaultFromImport: CreateVaultFromImportUseCase,
) : Reducer<ImportScreenAction.OnConfirmImport, ImportScreenState, ImportScreenAction, ImportScreenEvent> {

    override val actionClass = ImportScreenAction.OnConfirmImport::class

    override suspend fun reduce(
        action: ImportScreenAction.OnConfirmImport,
        getState: () -> ImportScreenState
    ): Reducer.Result<ImportScreenState, ImportScreenAction, ImportScreenEvent?> {
        val current = getState()
        if (current !is ImportScreenState.Loaded) {
            return Reducer.Result(state = current, action = null, event = null)
        }

        val items = current.parsedItems
            ?: return Reducer.Result(state = current, action = null, event = null)

        return when (current.target) {
            ImportTarget.MERGE_INTO_ACTIVE -> {
                when (val result = applyImport.execute(items, current.strategy)) {
                    is AppResult.Success -> {
                        val r = result.value
                        val verb = when (current.strategy) {
                            ImportStrategy.REPLACE -> "Replaced active vault with"
                            ImportStrategy.APPEND -> "Appended"
                            ImportStrategy.MERGE_BY_TITLE_USERNAME -> "Merged"
                        }
                        val tail = when (current.strategy) {
                            ImportStrategy.MERGE_BY_TITLE_USERNAME ->
                                "${r.added} item(s); skipped ${r.skipped} duplicate(s)."
                            else -> "${r.added} item(s)."
                        }
                        Reducer.Result(
                            state = resetState(current),
                            action = null,
                            event = ImportScreenEvent.ShowSuccess("$verb $tail"),
                        )
                    }

                    is AppResult.Failure -> Reducer.Result(
                        state = current.copy(isProcessing = false),
                        action = null,
                        event = ImportScreenEvent.ShowError(result.error.message ?: "Import failed."),
                    )
                }
            }

            ImportTarget.NEW_VAULT -> {
                val name = current.newVaultName.trim()
                if (current.password.isBlank() && current.selectedFormat.name != "DATA") {
                    // The new vault needs a master password. We re-use the password field;
                    // for CSV/JSON we hadn't yet collected one, so block here.
                    return Reducer.Result(
                        state = current.copy(passwordError = "A master password is required for the new vault."),
                        action = null,
                        event = null,
                    )
                }
                val password = if (current.password.isBlank()) {
                    // For DATA imports, prefer the source-file password if no override.
                    current.password
                } else {
                    current.password
                }
                when (val result = createVaultFromImport.execute(items, name, password)) {
                    is AppResult.Success -> Reducer.Result(
                        state = resetState(current),
                        action = null,
                        event = ImportScreenEvent.ShowSuccess(
                            "Created \"${result.value.name}\" with ${items.size} item(s)."
                        ),
                    )

                    is AppResult.Failure -> Reducer.Result(
                        state = current.copy(isProcessing = false),
                        action = null,
                        event = ImportScreenEvent.ShowError(result.error.message ?: "Failed to create vault."),
                    )
                }
            }
        }
    }

    private fun resetState(current: ImportScreenState.Loaded): ImportScreenState.Loaded =
        current.copy(
            isProcessing = false,
            pendingFile = null,
            parsedItems = null,
            conflictReport = null,
            isConfirmDialogVisible = false,
            password = "",
            passwordError = null,
            isPasswordVisible = false,
        )
}
