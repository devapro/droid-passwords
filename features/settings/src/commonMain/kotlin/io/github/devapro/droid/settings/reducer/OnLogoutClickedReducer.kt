package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.sync.SyncManager
import io.github.devapro.droid.data.sync.SyncScheduler
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

class OnLogoutClickedReducer(
    private val syncManager: SyncManager,
    private val syncScheduler: SyncScheduler
) : Reducer<SettingsScreenAction.OnLogoutClicked, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnLogoutClicked::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnLogoutClicked,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent?> {
        val currentState = getState()
        if (currentState !is SettingsScreenState.Success) {
            return Reducer.Result(currentState)
        }
        syncManager.logout()
        syncScheduler.stop()
        return Reducer.Result(
            state = currentState.copy(
                isLoggedIn = false,
                syncUsername = "",
                periodicSyncEnabled = false
            ),
            event = SettingsScreenEvent.ShowSuccess("Signed out")
        )
    }
}
