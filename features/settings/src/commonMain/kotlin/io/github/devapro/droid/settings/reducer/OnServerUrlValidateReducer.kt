package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.sync.SyncManager
import io.github.devapro.droid.data.sync.SyncStateStore
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

/**
 * Probes the server's health endpoint (HTTPS reachability) before persisting the URL.
 * Keeps the dialog open with an inline error if the server can't be reached, so the
 * user never saves a URL that won't work for sync.
 */
class OnServerUrlValidateReducer(
    private val syncManager: SyncManager,
    private val syncStateStore: SyncStateStore
) : Reducer<SettingsScreenAction.OnServerUrlValidate, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnServerUrlValidate::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnServerUrlValidate,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent?> {
        val currentState = getState()
        if (currentState !is SettingsScreenState.Success) {
            return Reducer.Result(currentState)
        }
        return when (val result = syncManager.checkServerHealth(action.url)) {
            is AppResult.Success -> {
                syncStateStore.setServerUrl(action.url)
                Reducer.Result(
                    state = currentState.copy(
                        syncServerUrl = action.url,
                        isServerUrlDialogVisible = false,
                        isServerUrlChecking = false,
                        serverUrlError = null
                    ),
                    event = SettingsScreenEvent.ShowSuccess("Server reachable — URL saved")
                )
            }

            is AppResult.Failure -> Reducer.Result(
                state = currentState.copy(
                    isServerUrlChecking = false,
                    serverUrlError = result.error.message ?: "Server is unreachable"
                )
            )
        }
    }
}
