package io.github.devapro.droid.vaultlist.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.vaultlist.model.VaultListScreenAction
import io.github.devapro.droid.vaultlist.model.VaultListScreenEvent
import io.github.devapro.droid.vaultlist.model.VaultListScreenState

class OnDismissAddMenuReducer
    : Reducer<VaultListScreenAction.OnDismissAddMenu, VaultListScreenState, VaultListScreenAction, VaultListScreenEvent> {

    override val actionClass = VaultListScreenAction.OnDismissAddMenu::class

    override suspend fun reduce(
        action: VaultListScreenAction.OnDismissAddMenu,
        getState: () -> VaultListScreenState
    ): Reducer.Result<VaultListScreenState, VaultListScreenAction.OnDismissAddMenu, VaultListScreenEvent?> {
        val current = getState()
        return if (current is VaultListScreenState.Loaded) {
            Reducer.Result(state = current.copy(showAddMenu = false), action = null, event = null)
        } else {
            Reducer.Result(state = current, action = null, event = null)
        }
    }
}
