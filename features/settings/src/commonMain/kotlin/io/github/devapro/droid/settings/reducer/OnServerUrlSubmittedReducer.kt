package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

/**
 * Validates the entered URL shape (non-empty, HTTPS) synchronously, then hands off to
 * [OnServerUrlValidate] which probes the server's health endpoint before saving.
 */
class OnServerUrlSubmittedReducer :
    Reducer<SettingsScreenAction.OnServerUrlSubmitted, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

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
            return Reducer.Result(state = currentState.copy(serverUrlError = "Server URL cannot be empty"))
        }
        if (!url.startsWith("https://", ignoreCase = true)) {
            return Reducer.Result(
                state = currentState.copy(serverUrlError = "Server URL must use HTTPS (e.g. https://192.168.1.50:8443)")
            )
        }
        return Reducer.Result(
            state = currentState.copy(isServerUrlChecking = true, serverUrlError = null),
            action = SettingsScreenAction.OnServerUrlValidate(url)
        )
    }
}
