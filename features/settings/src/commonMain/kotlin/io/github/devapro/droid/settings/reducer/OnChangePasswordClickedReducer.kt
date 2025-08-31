package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

/**
 * Reducer for handling the change password button click.
 * Shows the change password dialog by updating the state.
 */
class OnChangePasswordClickedReducer
    :
    Reducer<SettingsScreenAction.OnChangePasswordClicked, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnChangePasswordClicked::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnChangePasswordClicked,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent?> {
        val currentState = getState()

        return if (currentState is SettingsScreenState.Success) {
            Reducer.Result(
                state = currentState.copy(
                    isChangePasswordDialogVisible = true,
                    passwordChangeError = null // Clear any previous errors
                ),
                action = null,
                event = null
            )
        } else {
            Reducer.Result(
                state = currentState,
                action = null,
                event = null
            )
        }
    }
}