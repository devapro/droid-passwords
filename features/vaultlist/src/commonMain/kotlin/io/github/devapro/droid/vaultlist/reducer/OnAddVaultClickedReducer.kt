package io.github.devapro.droid.vaultlist.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.vaultlist.model.VaultListScreenAction
import io.github.devapro.droid.vaultlist.model.VaultListScreenEvent
import io.github.devapro.droid.vaultlist.model.VaultListScreenState

class OnAddVaultClickedReducer
    : Reducer<VaultListScreenAction.OnAddVaultClicked, VaultListScreenState, VaultListScreenAction, VaultListScreenEvent> {

    override val actionClass = VaultListScreenAction.OnAddVaultClicked::class

    override suspend fun reduce(
        action: VaultListScreenAction.OnAddVaultClicked,
        getState: () -> VaultListScreenState
    ): Reducer.Result<VaultListScreenState, VaultListScreenAction.OnAddVaultClicked, VaultListScreenEvent?> {
        val current = getState()
        return if (current is VaultListScreenState.Loaded) {
            Reducer.Result(state = current.copy(showAddMenu = true), action = null, event = null)
        } else {
            Reducer.Result(state = current, action = null, event = null)
        }
    }
}
