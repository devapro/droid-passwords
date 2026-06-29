package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

class OnDismissLinkMasterPasswordDialogReducer
    : Reducer<SettingsScreenAction.OnDismissLinkMasterPasswordDialog, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnDismissLinkMasterPasswordDialog::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnDismissLinkMasterPasswordDialog,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent?> {
        val currentState = getState()
        if (currentState !is SettingsScreenState.Success) {
            return Reducer.Result(currentState)
        }
        return Reducer.Result(
            state = currentState.copy(
                isLinkMasterPasswordDialogVisible = false,
                isLinkInProgress = false,
                linkMasterPasswordError = null
            )
        )
    }
}
