package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

class OnRenameDraftChangedReducer
    : Reducer<SettingsScreenAction.OnRenameDraftChanged, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnRenameDraftChanged::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnRenameDraftChanged,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction.OnRenameDraftChanged, SettingsScreenEvent?> {
        val current = getState()
        return if (current is SettingsScreenState.Success) {
            Reducer.Result(
                state = current.copy(renameDraft = action.name, renameError = null),
                action = null,
                event = null,
            )
        } else Reducer.Result(state = current, action = null, event = null)
    }
}
