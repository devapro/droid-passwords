package io.github.devapro.features.importexport.reducer

import io.github.devapro.core.mvi.AppResult
import io.github.devapro.core.mvi.Reducer
import io.github.devapro.data.vault.VaultFileRepository
import io.github.devapro.data.vault.VaultRuntimeRepository
import io.github.devapro.features.importexport.model.ImportExportScreenAction
import io.github.devapro.features.importexport.model.ImportExportScreenEvent
import io.github.devapro.features.importexport.model.ImportExportScreenState

class OnFileSelectedReducer(
    private val fileRepository: VaultFileRepository,
    private val repository: VaultRuntimeRepository
) : Reducer<ImportExportScreenAction.ExportFileSelected, ImportExportScreenState, ImportExportScreenAction, ImportExportScreenEvent> {

    override val actionClass = ImportExportScreenAction.ExportFileSelected::class

    override suspend fun reduce(
        action: ImportExportScreenAction.ExportFileSelected,
        getState: () -> ImportExportScreenState
    ): Reducer.Result<ImportExportScreenState, ImportExportScreenAction, ImportExportScreenEvent?> {
        val currentState = getState()
        return if (currentState is ImportExportScreenState.Loaded) {

            val result = fileRepository.saveVault(
                vaultModel = repository.getVault(),
                fileForExport = action.file
            )

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