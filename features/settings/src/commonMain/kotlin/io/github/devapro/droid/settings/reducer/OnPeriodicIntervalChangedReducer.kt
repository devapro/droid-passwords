package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.sync.SyncScheduler
import io.github.devapro.droid.data.sync.SyncStateStore
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

class OnPeriodicIntervalChangedReducer(
    private val syncStateStore: SyncStateStore,
    private val syncScheduler: SyncScheduler
) : Reducer<SettingsScreenAction.OnPeriodicIntervalChanged, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnPeriodicIntervalChanged::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnPeriodicIntervalChanged,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent?> {
        val currentState = getState()
        if (currentState !is SettingsScreenState.Success) {
            return Reducer.Result(currentState)
        }
        syncStateStore.setPeriodicIntervalMinutes(action.minutes)
        if (currentState.periodicSyncEnabled) {
            syncScheduler.start(action.minutes)
        }
        return Reducer.Result(currentState.copy(periodicSyncIntervalMinutes = action.minutes))
    }
}
