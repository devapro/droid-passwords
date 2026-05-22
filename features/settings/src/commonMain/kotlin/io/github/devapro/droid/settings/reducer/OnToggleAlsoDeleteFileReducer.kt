package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

class OnToggleAlsoDeleteFileReducer
    : Reducer<SettingsScreenAction.OnToggleAlsoDeleteFile, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnToggleAlsoDeleteFile::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnToggleAlsoDeleteFile,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction.OnToggleAlsoDeleteFile, SettingsScreenEvent?> {
        val current = getState()
        return if (current is SettingsScreenState.Success) {
            Reducer.Result(
                state = current.copy(removeAlsoDeleteFile = !current.removeAlsoDeleteFile),
                action = null,
                event = null,
            )
        } else Reducer.Result(state = current, action = null, event = null)
    }
}
