package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

class OnSyncFromServerClickedReducer :
    Reducer<SettingsScreenAction.OnSyncFromServerClicked, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnSyncFromServerClicked::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnSyncFromServerClicked,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent?> {
        val currentState = getState()
        if (currentState !is SettingsScreenState.Success || currentState.isSyncing) {
            return Reducer.Result(currentState)
        }
        return Reducer.Result(
            state = currentState.copy(isSyncing = true),
            action = SettingsScreenAction.OnSyncFromServerExecute
        )
    }
}
