package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.sync.SyncStateStore
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

class OnServerUrlSubmittedReducer(
    private val syncStateStore: SyncStateStore
) : Reducer<SettingsScreenAction.OnServerUrlSubmitted, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnServerUrlSubmitted::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnServerUrlSubmitted,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent?> {
        val currentState = getState()
        if (currentState !is SettingsScreenState.Success) {
            return Reducer.Result(currentState)
        }
        val url = action.url.trim()
        if (url.isEmpty()) {
            return Reducer.Result(
                state = currentState.copy(isServerUrlDialogVisible = false),
                event = SettingsScreenEvent.ShowError("Server URL cannot be empty")
            )
        }
        syncStateStore.setServerUrl(url)
        return Reducer.Result(
            state = currentState.copy(
                syncServerUrl = url,
                isServerUrlDialogVisible = false
            ),
            event = SettingsScreenEvent.ShowSuccess("Server URL saved")
        )
    }
}
