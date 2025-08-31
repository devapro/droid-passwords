package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

/**
 * Reducer for handling back navigation from the settings screen.
 * Triggers the NavigateBack event to return to the previous screen.
 */
class OnBackClickedReducer
    :
    Reducer<SettingsScreenAction.OnBackClicked, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnBackClicked::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnBackClicked,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent?> {

        return Reducer.Result(
            state = getState(),
            action = null,
            event = SettingsScreenEvent.NavigateBack
        )
    }
}