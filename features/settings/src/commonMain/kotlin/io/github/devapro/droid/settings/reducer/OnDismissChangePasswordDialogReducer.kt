package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

/**
 * Reducer for handling the dismissal of the change password dialog.
 * Hides the dialog and clears any password change errors.
 */
class OnDismissChangePasswordDialogReducer
    :
    Reducer<SettingsScreenAction.OnDismissChangePasswordDialog, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnDismissChangePasswordDialog::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnDismissChangePasswordDialog,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent?> {
        val currentState = getState()

        return if (currentState is SettingsScreenState.Success) {
            Reducer.Result(
                state = currentState.copy(
                    isChangePasswordDialogVisible = false,
                    passwordChangeError = null,
                    isChangingPassword = false // Reset loading state if dialog is dismissed
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