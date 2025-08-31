package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.SettingsRepository
import io.github.devapro.droid.data.ThemeManager
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

/**
 * Reducer for handling theme mode changes.
 * Updates the theme mode setting and applies the theme change immediately.
 */
class OnThemeModeChangedReducer(
    private val settingsRepository: SettingsRepository,
    private val themeManager: ThemeManager
) : Reducer<SettingsScreenAction.OnThemeModeChanged, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnThemeModeChanged::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnThemeModeChanged,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent?> {
        val currentState = getState()

        return if (currentState is SettingsScreenState.Success) {
            try {
                // Save the new theme mode setting
                settingsRepository.setThemeMode(action.mode)

                // Apply the theme change immediately through ThemeManager
                themeManager.setThemeMode(action.mode)

                Reducer.Result(
                    state = currentState.copy(themeMode = action.mode),
                    action = null,
                    event = null
                )
            } catch (e: Exception) {
                Reducer.Result(
                    state = currentState,
                    action = null,
                    event = SettingsScreenEvent.ShowError("Failed to update theme mode: ${e.message}")
                )
            }
        } else {
            Reducer.Result(
                state = currentState,
                action = null,
                event = null
            )
        }
    }
}