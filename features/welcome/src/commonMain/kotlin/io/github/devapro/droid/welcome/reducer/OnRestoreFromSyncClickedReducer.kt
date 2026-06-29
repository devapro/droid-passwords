package io.github.devapro.droid.welcome.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.sync.SyncStateStore
import io.github.devapro.droid.welcome.model.WelcomeScreenAction
import io.github.devapro.droid.welcome.model.WelcomeScreenEvent
import io.github.devapro.droid.welcome.model.WelcomeScreenState

class OnRestoreFromSyncClickedReducer(
    private val syncStateStore: SyncStateStore
) : Reducer<WelcomeScreenAction.OnRestoreFromSyncClicked, WelcomeScreenState, WelcomeScreenAction, WelcomeScreenEvent> {

    override val actionClass = WelcomeScreenAction.OnRestoreFromSyncClicked::class

    override suspend fun reduce(
        action: WelcomeScreenAction.OnRestoreFromSyncClicked,
        getState: () -> WelcomeScreenState
    ): Reducer.Result<WelcomeScreenState, WelcomeScreenAction, WelcomeScreenEvent?> {
        val currentState = getState()
        if (currentState !is WelcomeScreenState.Success || currentState.isVaultExists) {
            return Reducer.Result(currentState)
        }
        return if (syncStateStore.isLoggedIn()) {
            // Already signed in: open the vault list so the user can pick one of the
            // account's vaults (no master password prompt here — each vault is unlocked
            // when it is opened). The vault list is populated by the background sync.
            Reducer.Result(
                state = currentState,
                event = WelcomeScreenEvent.OnLoadVault
            )
        } else {
            Reducer.Result(
                state = currentState.copy(
                    isRestoreAuthDialogVisible = true,
                    restoreError = null
                )
            )
        }
    }
}
