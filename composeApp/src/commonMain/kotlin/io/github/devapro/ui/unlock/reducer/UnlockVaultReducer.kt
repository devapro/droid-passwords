package io.github.devapro.ui.unlock.reducer

import io.github.devapro.core.mvi.AppResult
import io.github.devapro.core.mvi.Reducer
import io.github.devapro.data.vault.VaultFileRepository
import io.github.devapro.data.vault.VaultRuntimeRepository
import io.github.devapro.ui.unlock.model.UnLockVaultScreenAction
import io.github.devapro.ui.unlock.model.UnLockVaultScreenEvent
import io.github.devapro.ui.unlock.model.UnLockVaultScreenState

class UnlockVaultReducer(
    private val vaultFileRepository: VaultFileRepository,
    private val runtimeRepository: VaultRuntimeRepository
) : Reducer<UnLockVaultScreenAction.UnlockVault, UnLockVaultScreenState, UnLockVaultScreenAction, UnLockVaultScreenEvent> {

    override val actionClass = UnLockVaultScreenAction.UnlockVault::class

    override suspend fun reduce(
        action: UnLockVaultScreenAction.UnlockVault,
        getState: () -> UnLockVaultScreenState
    ): Reducer.Result<UnLockVaultScreenState, UnLockVaultScreenAction, UnLockVaultScreenEvent> {
        val currentState = getState()

        return if (currentState is UnLockVaultScreenState.Loaded && currentState.isValid) {
            val readVaultResult = vaultFileRepository.getVault(action.password)
            when (readVaultResult) {
                is AppResult.Success -> {
                    val vault = readVaultResult.value
                    runtimeRepository.loadVault(vault)
                    Reducer.Result(
                        state = currentState.copy(isProcessing = false),
                        action = null,
                        event = UnLockVaultScreenEvent.UnlockSuccess
                    )
                }

                is AppResult.Failure -> {
                    Reducer.Result(
                        state = currentState.copy(isProcessing = false),
                        action = null,
                        event = UnLockVaultScreenEvent.ShowError("Failed to read the vault.")
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