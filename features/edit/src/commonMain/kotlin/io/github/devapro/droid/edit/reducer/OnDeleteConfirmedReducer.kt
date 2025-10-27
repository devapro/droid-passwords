package io.github.devapro.droid.edit.reducer

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.vault.VaultFileRepository
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import io.github.devapro.droid.edit.model.AddEditPasswordScreenAction
import io.github.devapro.droid.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.droid.edit.model.AddEditPasswordScreenState

class OnDeleteConfirmedReducer(
    private val runtimeRepository: VaultRuntimeRepository,
    private val repository: VaultFileRepository
) : Reducer<AddEditPasswordScreenAction.OnDeleteConfirmed, AddEditPasswordScreenState.Success, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {

    override val actionClass = AddEditPasswordScreenAction.OnDeleteConfirmed::class

    override suspend fun reduce(
        action: AddEditPasswordScreenAction.OnDeleteConfirmed,
        getState: () -> AddEditPasswordScreenState.Success
    ): Reducer.Result<AddEditPasswordScreenState.Success, AddEditPasswordScreenAction.OnDeleteConfirmed, AddEditPasswordScreenEvent?> {
        val currentState = getState()

        return if (currentState.isEditMode && currentState.itemId != null) {
            runtimeRepository.deleteVaultById(currentState.itemId)
            val result = repository.saveVault(runtimeRepository.getVault())
            when (result) {
                is AppResult.Success -> {
                    Reducer.Result(
                        state = currentState.copy(
                            showDeleteConfirmation = false,
                            isSaving = true
                        ),
                        action = null,
                        event = AddEditPasswordScreenEvent.DeleteSuccess(currentState.itemId)
                    )
                }
                is AppResult.Failure -> {
                    Reducer.Result(
                        state = currentState.copy(showDeleteConfirmation = false),
                        action = null,
                        event = AddEditPasswordScreenEvent.ShowMessage("Cannot delete item")
                    )
                }
            }
        } else {
            Reducer.Result(
                state = currentState.copy(showDeleteConfirmation = false),
                action = null,
                event = AddEditPasswordScreenEvent.ShowMessage("Cannot delete item")
            )
        }
    }
}
