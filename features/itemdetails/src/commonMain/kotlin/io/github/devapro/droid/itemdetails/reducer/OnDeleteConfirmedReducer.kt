package io.github.devapro.droid.itemdetails.reducer

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.vault.VaultFileRepository
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import io.github.devapro.droid.itemdetails.model.PasswordDetailScreenAction
import io.github.devapro.droid.itemdetails.model.PasswordDetailScreenEvent
import io.github.devapro.droid.itemdetails.model.PasswordDetailScreenState

class OnDeleteConfirmedReducer(
    private val runtimeRepository: VaultRuntimeRepository,
    private val repository: VaultFileRepository
): Reducer<PasswordDetailScreenAction.OnDeleteConfirmed, PasswordDetailScreenState.Success, PasswordDetailScreenAction, PasswordDetailScreenEvent> {

    override val actionClass = PasswordDetailScreenAction.OnDeleteConfirmed::class

    override suspend fun reduce(
        action: PasswordDetailScreenAction.OnDeleteConfirmed,
        getState: () -> PasswordDetailScreenState.Success
    ): Reducer.Result<PasswordDetailScreenState.Success, PasswordDetailScreenAction.OnDeleteConfirmed, PasswordDetailScreenEvent?> {
        val currentState = getState()

        runtimeRepository.deleteVaultById(currentState.item.id)
        val result = repository.saveVault(runtimeRepository.getVault())
        return when(result) {
            is AppResult.Success -> {
                Reducer.Result(
                    state = currentState.copy(
                        showDeleteConfirmation = false,
                        isLoading = true
                    ),
                    action = null,
                    event = PasswordDetailScreenEvent.DeleteItem(currentState.item)
                )
            }
            is AppResult.Failure -> {
                Reducer.Result(
                    state = currentState,
                    action = null,
                    event = PasswordDetailScreenEvent.ShowMessage("Cannot delete item")
                )
            }
        }
    }
} 