package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

class OnSyncNowClickedReducer :
    Reducer<SettingsScreenAction.OnSyncNowClicked, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnSyncNowClicked::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnSyncNowClicked,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent?> {
        val currentState = getState()
        if (currentState !is SettingsScreenState.Success || currentState.isSyncing) {
            return Reducer.Result(currentState)
        }
        // Show the loading state first, then run the sync via the chained action.
        return Reducer.Result(
            state = currentState.copy(isSyncing = true),
            action = SettingsScreenAction.OnSyncNowExecute
        )
    }
}
