package io.github.devapro.droid.vaultlist.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.sync.SyncManager
import io.github.devapro.droid.data.sync.SyncScheduler
import io.github.devapro.droid.vaultlist.model.VaultListScreenAction
import io.github.devapro.droid.vaultlist.model.VaultListScreenEvent
import io.github.devapro.droid.vaultlist.model.VaultListScreenState

/**
 * Signs out of the sync account from the vault list (the locked-state hub). Clears the
 * stored credentials and stops periodic sync; local vaults stay on the device and remain
 * openable, so this only updates the logged-in flag rather than navigating away.
 */
class OnLogoutClickedReducer(
    private val syncManager: SyncManager,
    private val syncScheduler: SyncScheduler,
) : Reducer<VaultListScreenAction.OnLogoutClicked, VaultListScreenState, VaultListScreenAction, VaultListScreenEvent> {

    override val actionClass = VaultListScreenAction.OnLogoutClicked::class

    override suspend fun reduce(
        action: VaultListScreenAction.OnLogoutClicked,
        getState: () -> VaultListScreenState
    ): Reducer.Result<VaultListScreenState, VaultListScreenAction, VaultListScreenEvent?> {
        val currentState = getState()
        if (currentState !is VaultListScreenState.Loaded) {
            return Reducer.Result(state = currentState, action = null, event = null)
        }
        syncManager.logout()
        syncScheduler.stop()
        return Reducer.Result(
            state = currentState.copy(isLoggedIn = false),
            action = null,
            event = VaultListScreenEvent.ShowMessage("Signed out"),
        )
    }
}
