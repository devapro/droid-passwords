package io.github.devapro.droid.vaultlist.reducer

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.sync.SyncManager
import io.github.devapro.droid.data.sync.SyncStateStore
import io.github.devapro.droid.data.vault.VaultRegistryRepository
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import io.github.devapro.droid.vaultlist.model.VaultListItem
import io.github.devapro.droid.vaultlist.model.VaultListScreenAction
import io.github.devapro.droid.vaultlist.model.VaultListScreenEvent
import io.github.devapro.droid.vaultlist.model.VaultListScreenState

class OnSyncVaultsReducer(
    private val syncManager: SyncManager,
    private val registryRepository: VaultRegistryRepository,
    private val runtimeRepository: VaultRuntimeRepository,
    private val syncStateStore: SyncStateStore,
) : Reducer<VaultListScreenAction.OnSyncVaults, VaultListScreenState, VaultListScreenAction, VaultListScreenEvent> {

    override val actionClass = VaultListScreenAction.OnSyncVaults::class

    override suspend fun reduce(
        action: VaultListScreenAction.OnSyncVaults,
        getState: () -> VaultListScreenState
    ): Reducer.Result<VaultListScreenState, VaultListScreenAction, VaultListScreenEvent?> {
        // Discover any vaults created on other devices and register them locally as
        // selectable (locked) entries. Suspends on the network — the spinner set by the
        // caller stays visible until this returns.
        val discovery = syncManager.discoverVaults()

        val registry = registryRepository.getRegistry()
        val activeId = runtimeRepository.getActiveVaultId()
            ?: registryRepository.getActiveVaultId()
        val state = VaultListScreenState.Loaded(
            vaults = registry.map { descriptor ->
                VaultListItem(
                    descriptor = descriptor,
                    isLoaded = runtimeRepository.isLoaded(descriptor.id),
                )
            },
            activeVaultId = activeId,
            isLoggedIn = syncStateStore.isLoggedIn(),
            isSyncing = false,
        )

        val event = (discovery as? AppResult.Failure)
            ?.let { VaultListScreenEvent.ShowError(it.error.message ?: "Couldn't reach the sync server") }

        return Reducer.Result(state = state, action = null, event = event)
    }
}
