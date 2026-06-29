package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.sync.SyncScheduler
import io.github.devapro.droid.data.sync.SyncStateStore
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

class OnPeriodicSyncToggledReducer(
    private val syncStateStore: SyncStateStore,
    private val syncScheduler: SyncScheduler
) : Reducer<SettingsScreenAction.OnPeriodicSyncToggled, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnPeriodicSyncToggled::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnPeriodicSyncToggled,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent?> {
        val currentState = getState()
        if (currentState !is SettingsScreenState.Success) {
            return Reducer.Result(currentState)
        }
        if (action.enabled && !currentState.isLoggedIn) {
            return Reducer.Result(
                state = currentState,
                event = SettingsScreenEvent.ShowError("Sign in to a sync server first")
            )
        }
        syncStateStore.setPeriodicEnabled(action.enabled)
        if (action.enabled) {
            syncScheduler.start(currentState.periodicSyncIntervalMinutes)
        } else {
            syncScheduler.stop()
        }
        return Reducer.Result(currentState.copy(periodicSyncEnabled = action.enabled))
    }
}
