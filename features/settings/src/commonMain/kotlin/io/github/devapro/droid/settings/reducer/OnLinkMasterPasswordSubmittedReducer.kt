package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

class OnLinkMasterPasswordSubmittedReducer
    : Reducer<SettingsScreenAction.OnLinkMasterPasswordSubmitted, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnLinkMasterPasswordSubmitted::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnLinkMasterPasswordSubmitted,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent?> {
        val currentState = getState()
        if (currentState !is SettingsScreenState.Success || currentState.isLinkInProgress) {
            return Reducer.Result(currentState)
        }
        if (action.masterPassword.isBlank()) {
            return Reducer.Result(currentState.copy(linkMasterPasswordError = "Master password cannot be empty"))
        }
        // Show the loading state first, then run the restore via the chained action.
        return Reducer.Result(
            state = currentState.copy(isLinkInProgress = true, linkMasterPasswordError = null),
            action = SettingsScreenAction.OnLinkMasterPasswordExecute(masterPassword = action.masterPassword)
        )
    }
}
