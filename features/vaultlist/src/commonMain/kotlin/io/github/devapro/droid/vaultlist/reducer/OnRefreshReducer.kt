package io.github.devapro.droid.vaultlist.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.sync.SyncStateStore
import io.github.devapro.droid.data.vault.VaultRegistryRepository
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import io.github.devapro.droid.vaultlist.model.VaultListItem
import io.github.devapro.droid.vaultlist.model.VaultListScreenAction
import io.github.devapro.droid.vaultlist.model.VaultListScreenEvent
import io.github.devapro.droid.vaultlist.model.VaultListScreenState

class OnRefreshReducer(
    private val registryRepository: VaultRegistryRepository,
    private val runtimeRepository: VaultRuntimeRepository,
    private val syncStateStore: SyncStateStore,
) : Reducer<VaultListScreenAction.OnRefresh, VaultListScreenState, VaultListScreenAction, VaultListScreenEvent> {

    override val actionClass = VaultListScreenAction.OnRefresh::class

    override suspend fun reduce(
        action: VaultListScreenAction.OnRefresh,
        getState: () -> VaultListScreenState
    ): Reducer.Result<VaultListScreenState, VaultListScreenAction, VaultListScreenEvent?> {
        // Ignore a refresh while one is already in flight so repeated pulls don't stack
        // up back-to-back server round-trips.
        val currentState = getState()
        if (currentState is VaultListScreenState.Loaded && currentState.isSyncing) {
            return Reducer.Result(state = currentState, action = null, event = null)
        }

        val loggedIn = syncStateStore.isLoggedIn()
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
            isLoggedIn = loggedIn,
            isSyncing = loggedIn,
        )
        // Pull the latest vault list from the server (chained) while showing the spinner.
        return Reducer.Result(
            state = state,
            action = if (loggedIn) VaultListScreenAction.OnSyncVaults else null,
            event = null,
        )
    }
}
