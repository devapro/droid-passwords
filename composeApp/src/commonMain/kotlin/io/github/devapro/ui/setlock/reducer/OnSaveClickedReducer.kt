package io.github.devapro.ui.setlock.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.data.vault.VaultRepository
import io.github.devapro.ui.setlock.model.SetLockPasswordScreenAction
import io.github.devapro.ui.setlock.model.SetLockPasswordScreenEvent
import io.github.devapro.ui.setlock.model.SetLockPasswordScreenState

class OnSaveClickedReducer(
    private val vaultRepository: VaultRepository
) : Reducer<SetLockPasswordScreenAction.OnSaveClicked, SetLockPasswordScreenState, SetLockPasswordScreenAction, SetLockPasswordScreenEvent> {

    override val actionClass = SetLockPasswordScreenAction.OnSaveClicked::class

    override suspend fun reduce(
        action: SetLockPasswordScreenAction.OnSaveClicked,
        getState: () -> SetLockPasswordScreenState
    ): Reducer.Result<SetLockPasswordScreenState, SetLockPasswordScreenAction.OnSaveClicked, SetLockPasswordScreenEvent?> {
        val currentState = getState()

        return if (currentState is SetLockPasswordScreenState.Success && currentState.isValid && !currentState.isProcessing) {
            // Create a new vault or change the password
            val isSuccess = if (currentState.isNewVault) {
                vaultRepository.createVault(currentState.newPassword)
            } else {
                vaultRepository.changePassword(
                    oldPassword = currentState.currentPassword,
                    newPassword = currentState.newPassword
                )
            }
            Reducer.Result(
                state = currentState.copy(isProcessing = true),
                action = null,
                event = SetLockPasswordScreenEvent.SavePassword(
                    isNewVault = currentState.isNewVault,
                    oldPassword = if (currentState.isNewVault) currentState.currentPassword else null,
                    newPassword = currentState.newPassword
                )
            )
        } else {
            Reducer.Result(
                state = currentState,
                action = null,
                event = null
            )
        }
    }
} 