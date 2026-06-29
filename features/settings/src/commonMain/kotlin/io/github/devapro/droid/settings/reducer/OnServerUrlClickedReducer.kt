package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

class OnServerUrlClickedReducer :
    Reducer<SettingsScreenAction.OnServerUrlClicked, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnServerUrlClicked::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnServerUrlClicked,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent?> {
        val currentState = getState()
        return if (currentState is SettingsScreenState.Success) {
            Reducer.Result(
                state = currentState.copy(
                    isServerUrlDialogVisible = true,
                    isServerUrlChecking = false,
                    serverUrlError = null
                )
            )
        } else {
            Reducer.Result(currentState)
        }
    }
}
