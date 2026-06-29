package io.github.devapro.droid.setlock.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.vault.VaultRegistryRepository
import io.github.devapro.droid.setlock.model.SetLockPasswordScreenAction
import io.github.devapro.droid.setlock.model.SetLockPasswordScreenEvent
import io.github.devapro.droid.setlock.model.SetLockPasswordScreenState

class InitScreenReducer(
    @Suppress("UNUSED_PARAMETER") vaultRegistryRepository: VaultRegistryRepository,
) : Reducer<SetLockPasswordScreenAction.InitScreen, SetLockPasswordScreenState, SetLockPasswordScreenAction, SetLockPasswordScreenEvent> {

    override val actionClass = SetLockPasswordScreenAction.InitScreen::class

    override suspend fun reduce(
        action: SetLockPasswordScreenAction.InitScreen,
        getState: () -> SetLockPasswordScreenState
    ): Reducer.Result<SetLockPasswordScreenState, SetLockPasswordScreenAction.InitScreen, SetLockPasswordScreenEvent?> {
        // Setting a password from this screen always means creating a new vault.
        // Existing-vault password changes go through Settings' change-password dialog.
        val isVaultExists = false
        return Reducer.Result(
            state = SetLockPasswordScreenState.Success(
                isVaultExists = isVaultExists,
                vaultName = "",
                currentPassword = "",
                newPassword = "",
                confirmPassword = "",
                isCurrentPasswordVisible = false,
                isNewPasswordVisible = false,
                isConfirmPasswordVisible = false,
                isProcessing = false,
                vaultNameError = null,
                currentPasswordError = null,
                newPasswordError = null,
                confirmPasswordError = null,
                isValid = false
            ),
            action = null,
            event = null
        )
    }
}
