package io.github.devapro.droid.setlock.reducer

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.LockManager
import io.github.devapro.droid.data.vault.VaultDescriptor
import io.github.devapro.droid.data.vault.VaultFileRepository
import io.github.devapro.droid.data.vault.VaultRegistryRepository
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import io.github.devapro.droid.setlock.model.SetLockPasswordScreenAction
import io.github.devapro.droid.setlock.model.SetLockPasswordScreenEvent
import io.github.devapro.droid.setlock.model.SetLockPasswordScreenState

class OnSaveClickedReducer(
    private val vaultFileRepository: VaultFileRepository,
    private val vaultRegistryRepository: VaultRegistryRepository,
    private val runtimeRepository: VaultRuntimeRepository
) : Reducer<SetLockPasswordScreenAction.OnSaveClicked, SetLockPasswordScreenState, SetLockPasswordScreenAction, SetLockPasswordScreenEvent> {

    override val actionClass = SetLockPasswordScreenAction.OnSaveClicked::class

    override suspend fun reduce(
        action: SetLockPasswordScreenAction.OnSaveClicked,
        getState: () -> SetLockPasswordScreenState
    ): Reducer.Result<SetLockPasswordScreenState, SetLockPasswordScreenAction.OnSaveClicked, SetLockPasswordScreenEvent?> {
        val currentState = getState()

        if (currentState !is SetLockPasswordScreenState.Success
            || !currentState.isValid
            || currentState.isProcessing
        ) {
            return Reducer.Result(state = currentState, action = null, event = null)
        }

        val isExisting = currentState.isVaultExists
        val descriptor = if (isExisting) {
            vaultRegistryRepository.requireActiveDescriptor()
        } else {
            val now = nowMillis()
            val id = VaultDescriptor.newId()
            VaultDescriptor(
                id = id,
                name = currentState.vaultName.trim(),
                fileName = VaultDescriptor.newFileName(id),
                createdAt = now,
                updatedAt = now,
            )
        }

        val operationResult: AppResult<*> = if (isExisting) {
            vaultFileRepository.changePassword(
                descriptor = descriptor,
                oldPassword = currentState.currentPassword,
                newPassword = currentState.newPassword,
            )
        } else {
            vaultFileRepository.createVault(
                descriptor = descriptor,
                password = currentState.newPassword,
            )
        }

        if (operationResult is AppResult.Failure) {
            return Reducer.Result(
                state = currentState.copy(isProcessing = false),
                action = null,
                event = SetLockPasswordScreenEvent.ShowError("Failed to save the vault.")
            )
        }

        if (!isExisting) {
            vaultRegistryRepository.addVault(descriptor)
            vaultRegistryRepository.setActiveVaultId(descriptor.id)
        }

        val readVaultResult = vaultFileRepository.getVault(descriptor, currentState.newPassword)
        return when (readVaultResult) {
            is AppResult.Success -> {
                runtimeRepository.loadVault(descriptor, readVaultResult.value)
                runtimeRepository.setActiveVault(descriptor.id)
                LockManager.onVaultUnlocked()
                Reducer.Result(
                    state = currentState.copy(isProcessing = false),
                    action = null,
                    event = SetLockPasswordScreenEvent.ShowSuccess
                )
            }

            is AppResult.Failure -> {
                Reducer.Result(
                    state = currentState.copy(isProcessing = false),
                    action = null,
                    event = SetLockPasswordScreenEvent.ShowError("Failed to read the vault.")
                )
            }
        }
    }

    private fun nowMillis(): Long {
        @OptIn(kotlin.time.ExperimentalTime::class)
        return kotlin.time.Clock.System.now().toEpochMilliseconds()
    }
}
