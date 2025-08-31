package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

/**
 * Reducer for handling the file path button click.
 * Shows the file path selection dialog and triggers the file picker event.
 */
class OnFilePathClickedReducer
    :
    Reducer<SettingsScreenAction.OnFilePathClicked, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnFilePathClicked::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnFilePathClicked,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent?> {
        val currentState = getState()

        return if (currentState is SettingsScreenState.Success) {
            Reducer.Result(
                state = currentState.copy(
                    isFilePathDialogVisible = true,
                    filePathChangeError = null // Clear any previous errors
                ),
                action = null,
                event = SettingsScreenEvent.ShowFilePathPicker
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