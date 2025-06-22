package io.github.devapro.ui.setlock.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.data.vault.VaultFileRepository
import io.github.devapro.data.vault.VaultRuntimeRepository
import io.github.devapro.ui.setlock.model.SetLockPasswordScreenAction
import io.github.devapro.ui.setlock.model.SetLockPasswordScreenEvent
import io.github.devapro.ui.setlock.model.SetLockPasswordScreenState

class OnSaveClickedReducer(
    private val vaultFileRepository: VaultFileRepository,
    private val runtimeRepository: VaultRuntimeRepository
) : Reducer<SetLockPasswordScreenAction.OnSaveClicked, SetLockPasswordScreenState, SetLockPasswordScreenAction, SetLockPasswordScreenEvent> {

    override val actionClass = SetLockPasswordScreenAction.OnSaveClicked::class

    override suspend fun reduce(
        action: SetLockPasswordScreenAction.OnSaveClicked,
        getState: () -> SetLockPasswordScreenState
    ): Reducer.Result<SetLockPasswordScreenState, SetLockPasswordScreenAction.OnSaveClicked, SetLockPasswordScreenEvent?> {
        val currentState = getState()

        return if (currentState is SetLockPasswordScreenState.Success && currentState.isValid && !currentState.isProcessing) {
            // Create a new vault or change the password
            vaultFileRepository.createVault(currentState.newPassword)
            val isSuccess = if (currentState.isNewVault) {
                vaultFileRepository.createVault(currentState.newPassword)
            } else {
                vaultFileRepository.changePassword(
                    oldPassword = currentState.currentPassword,
                    newPassword = currentState.newPassword
                )
            }
            if (isSuccess) {
                val vault = vaultFileRepository.getVault(currentState.newPassword)
                runtimeRepository.loadVault(vault)
                Reducer.Result(
                    state = currentState.copy(isProcessing = false),
                    action = null,
                    event = SetLockPasswordScreenEvent.ShowSuccess
                )
            } else {
                // If the operation failed, return an error event
                Reducer.Result(
                    state = currentState.copy(isProcessing = false),
                    action = null,
                    event = SetLockPasswordScreenEvent.ShowError("Failed to save the vault.")
                )
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