package io.github.devapro.droid.unlock.reducer

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.sync.SyncScheduler
import io.github.devapro.droid.data.vault.VaultFileRepository
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import io.github.devapro.droid.unlock.model.UnLockVaultScreenAction
import io.github.devapro.droid.unlock.model.UnLockVaultScreenEvent
import io.github.devapro.droid.unlock.model.UnLockVaultScreenState

class UnlockVaultReducer(
    private val vaultFileRepository: VaultFileRepository,
    private val runtimeRepository: VaultRuntimeRepository,
    private val syncScheduler: SyncScheduler
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
                    // Kick off periodic sync (if enabled) now that the vault is unlocked.
                    syncScheduler.startIfEnabled()
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
                        event = UnLockVaultScreenEvent.ShowError("Wrong password.")
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