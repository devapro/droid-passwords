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
        // Read descriptor + contents atomically so a concurrent background sync can
        // never pair this vault's descriptor with another vault's items on disk.
        val snapshot = runtimeRepository.getActiveSnapshot()
        val result = if (snapshot == null) {
            AppResult.Failure(Exception("No active vault"))
        } else {
            repository.saveVault(descriptor = snapshot.descriptor, vaultModel = snapshot.vault)
        }
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