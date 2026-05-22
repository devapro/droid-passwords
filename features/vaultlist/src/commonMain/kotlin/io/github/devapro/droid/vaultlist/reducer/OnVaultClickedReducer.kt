package io.github.devapro.droid.vaultlist.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.vault.VaultRegistryRepository
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import io.github.devapro.droid.vaultlist.model.VaultListScreenAction
import io.github.devapro.droid.vaultlist.model.VaultListScreenEvent
import io.github.devapro.droid.vaultlist.model.VaultListScreenState

class OnVaultClickedReducer(
    private val runtimeRepository: VaultRuntimeRepository,
    private val registryRepository: VaultRegistryRepository,
) : Reducer<VaultListScreenAction.OnVaultClicked, VaultListScreenState, VaultListScreenAction, VaultListScreenEvent> {

    override val actionClass = VaultListScreenAction.OnVaultClicked::class

    override suspend fun reduce(
        action: VaultListScreenAction.OnVaultClicked,
        getState: () -> VaultListScreenState
    ): Reducer.Result<VaultListScreenState, VaultListScreenAction.OnVaultClicked, VaultListScreenEvent?> {
        val descriptor = action.descriptor
        return if (runtimeRepository.isLoaded(descriptor.id)) {
            runtimeRepository.setActiveVault(descriptor.id)
            registryRepository.setActiveVaultId(descriptor.id)
            Reducer.Result(
                state = getState(),
                action = null,
                event = VaultListScreenEvent.NavigateToTags,
            )
        } else {
            Reducer.Result(
                state = getState(),
                action = null,
                event = VaultListScreenEvent.NavigateToUnlock(descriptor.id),
            )
        }
    }
}
