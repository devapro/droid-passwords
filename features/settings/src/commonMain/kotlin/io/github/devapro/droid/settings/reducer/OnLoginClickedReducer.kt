package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

class OnLoginClickedReducer :
    Reducer<SettingsScreenAction.OnLoginClicked, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnLoginClicked::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnLoginClicked,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent?> {
        val currentState = getState()
        return if (currentState is SettingsScreenState.Success) {
            Reducer.Result(currentState.copy(isAuthDialogVisible = true, authError = null))
        } else {
            Reducer.Result(currentState)
        }
    }
}
