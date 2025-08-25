package io.github.devapro.features.setlock.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.vault.VaultFileRepository
import io.github.devapro.features.setlock.model.SetLockPasswordScreenAction
import io.github.devapro.features.setlock.model.SetLockPasswordScreenEvent
import io.github.devapro.features.setlock.model.SetLockPasswordScreenState

class InitScreenReducer(
    private val vaultFileRepository: VaultFileRepository
) : Reducer<SetLockPasswordScreenAction.InitScreen, SetLockPasswordScreenState, SetLockPasswordScreenAction, SetLockPasswordScreenEvent> {

    override val actionClass = SetLockPasswordScreenAction.InitScreen::class

    override suspend fun reduce(
        action: SetLockPasswordScreenAction.InitScreen,
        getState: () -> SetLockPasswordScreenState
    ): Reducer.Result<SetLockPasswordScreenState, SetLockPasswordScreenAction.InitScreen, SetLockPasswordScreenEvent?> {
        val isVaultExists = vaultFileRepository.isVaultExists()
        return Reducer.Result(
            state = SetLockPasswordScreenState.Success(
                isVaultExists = isVaultExists,
                currentPassword = "",
                newPassword = "",
                confirmPassword = "",
                isCurrentPasswordVisible = false,
                isNewPasswordVisible = false,
                isConfirmPasswordVisible = false,
                isProcessing = false,
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