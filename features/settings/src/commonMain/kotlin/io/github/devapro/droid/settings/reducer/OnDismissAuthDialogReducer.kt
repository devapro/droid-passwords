package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

class OnDismissAuthDialogReducer :
    Reducer<SettingsScreenAction.OnDismissAuthDialog, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnDismissAuthDialog::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnDismissAuthDialog,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent?> {
        val currentState = getState()
        return if (currentState is SettingsScreenState.Success) {
            Reducer.Result(currentState.copy(isAuthDialogVisible = false, authError = null))
        } else {
            Reducer.Result(currentState)
        }
    }
}
