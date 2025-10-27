package io.github.devapro.droid.edit.reducer

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.vault.VaultFileRepository
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import io.github.devapro.droid.edit.model.AddEditPasswordScreenAction
import io.github.devapro.droid.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.droid.edit.model.AddEditPasswordScreenState

class OnDeleteClickedReducer(
    private val runtimeRepository: VaultRuntimeRepository,
    private val repository: VaultFileRepository
) : Reducer<AddEditPasswordScreenAction.OnDeleteClicked, AddEditPasswordScreenState.Success, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {

    override val actionClass = AddEditPasswordScreenAction.OnDeleteClicked::class

    override suspend fun reduce(
        action: AddEditPasswordScreenAction.OnDeleteClicked,
        getState: () -> AddEditPasswordScreenState.Success
    ): Reducer.Result<AddEditPasswordScreenState.Success, AddEditPasswordScreenAction.OnDeleteClicked, AddEditPasswordScreenEvent?> {
        val currentState = getState()

        return if (currentState.isEditMode && currentState.itemId != null) {
            runtimeRepository.deleteVaultById(currentState.itemId)
            val result = repository.saveVault(runtimeRepository.getVault())
            when (result) {
                is AppResult.Success -> {
                    Reducer.Result(
                        state = currentState.copy(isSaving = true),
                        action = null,
                        event = AddEditPasswordScreenEvent.DeleteSuccess(currentState.itemId)
                    )
                }
                is AppResult.Failure -> {
                    Reducer.Result(
                        state = currentState,
                        action = null,
                        event = AddEditPasswordScreenEvent.ShowMessage("Cannot delete item")
                    )
                }
            }
        } else {
            Reducer.Result(
                state = currentState,
                action = null,
                event = AddEditPasswordScreenEvent.ShowMessage("Cannot delete item")
            )
        }
    }
} 