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
    ): Reducer.Result<VaultListScreenState, VaultListScreenAction.OnRefresh, VaultListScreenEvent?> {
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
        )
        return Reducer.Result(state = state, action = null, event = null)
    }
}
