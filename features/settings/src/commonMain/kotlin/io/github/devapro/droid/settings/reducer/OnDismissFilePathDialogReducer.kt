package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

/**
 * Reducer for handling the dismissal of the file path selection dialog.
 * Hides the dialog and clears any file path change errors.
 */
class OnDismissFilePathDialogReducer
    :
    Reducer<SettingsScreenAction.OnDismissFilePathDialog, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnDismissFilePathDialog::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnDismissFilePathDialog,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent?> {
        val currentState = getState()

        return if (currentState is SettingsScreenState.Success) {
            Reducer.Result(
                state = currentState.copy(
                    isFilePathDialogVisible = false,
                    filePathChangeError = null
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