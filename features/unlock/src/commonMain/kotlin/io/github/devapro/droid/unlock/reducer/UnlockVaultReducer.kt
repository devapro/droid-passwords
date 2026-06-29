package io.github.devapro.droid.unlock.reducer

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.LockManager
import io.github.devapro.droid.data.sync.SyncManager
import io.github.devapro.droid.data.sync.SyncScheduler
import io.github.devapro.droid.data.vault.VaultFileRepository
import io.github.devapro.droid.data.vault.VaultRegistryRepository
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import io.github.devapro.droid.unlock.model.UnLockVaultScreenAction
import io.github.devapro.droid.unlock.model.UnLockVaultScreenEvent
import io.github.devapro.droid.unlock.model.UnLockVaultScreenState

class UnlockVaultReducer(
    private val vaultFileRepository: VaultFileRepository,
    private val vaultRegistryRepository: VaultRegistryRepository,
    private val runtimeRepository: VaultRuntimeRepository,
    private val syncManager: SyncManager,
    private val syncScheduler: SyncScheduler
) : Reducer<UnLockVaultScreenAction.UnlockVault, UnLockVaultScreenState, UnLockVaultScreenAction, UnLockVaultScreenEvent> {

    override val actionClass = UnLockVaultScreenAction.UnlockVault::class

    override suspend fun reduce(
        action: UnLockVaultScreenAction.UnlockVault,
        getState: () -> UnLockVaultScreenState
    ): Reducer.Result<UnLockVaultScreenState, UnLockVaultScreenAction, UnLockVaultScreenEvent> {
        val currentState = getState()

        if (currentState !is UnLockVaultScreenState.Loaded || !currentState.isValid) {
            return Reducer.Result(state = currentState, action = null, event = null)
        }

        val descriptor = currentState.vaultId
            ?.let { vaultRegistryRepository.getDescriptor(it) }
            ?: runCatching { vaultRegistryRepository.requireActiveDescriptor() }.getOrNull()

        if (descriptor == null) {
            return Reducer.Result(
                state = currentState.copy(isProcessing = false),
                action = null,
                event = UnLockVaultScreenEvent.ShowError("No vault available.")
            )
        }

        // A placeholder discovered during sync has no local file yet: open it by
        // downloading and decrypting its contents from the server with this password.
        if (descriptor.pendingRestore) {
            return when (val result = syncManager.downloadPendingVault(descriptor.id, action.password)) {
                is AppResult.Success -> {
                    LockManager.onVaultUnlocked()
                    syncScheduler.startIfEnabled()
                    Reducer.Result(
                        state = currentState.copy(isProcessing = false),
                        action = null,
                        event = UnLockVaultScreenEvent.UnlockSuccess
                    )
                }

                is AppResult.Failure -> Reducer.Result(
                    state = currentState.copy(isProcessing = false),
                    action = null,
                    event = UnLockVaultScreenEvent.ShowError(result.error.message ?: "Couldn't open vault.")
                )
            }
        }

        val readResult = vaultFileRepository.getVault(descriptor, action.password)
        return when (readResult) {
            is AppResult.Success -> {
                runtimeRepository.loadVault(descriptor, readResult.value)
                runtimeRepository.setActiveVault(descriptor.id)
                vaultRegistryRepository.setActiveVaultId(descriptor.id)
                LockManager.onVaultUnlocked()
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
    }
}
