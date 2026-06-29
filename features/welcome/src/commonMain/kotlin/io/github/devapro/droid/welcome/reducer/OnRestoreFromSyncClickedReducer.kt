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
            Reducer.Result(
                state = currentState.copy(
                    isMasterPasswordDialogVisible = true,
                    restoreError = null
                )
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
