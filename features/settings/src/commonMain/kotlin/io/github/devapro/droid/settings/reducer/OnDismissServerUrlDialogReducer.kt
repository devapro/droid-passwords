package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

class OnDismissServerUrlDialogReducer :
    Reducer<SettingsScreenAction.OnDismissServerUrlDialog, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnDismissServerUrlDialog::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnDismissServerUrlDialog,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent?> {
        val currentState = getState()
        return if (currentState is SettingsScreenState.Success) {
            Reducer.Result(
                state = currentState.copy(
                    isServerUrlDialogVisible = false,
                    isServerUrlChecking = false,
                    serverUrlError = null
                )
            )
        } else {
            Reducer.Result(currentState)
        }
    }
}
