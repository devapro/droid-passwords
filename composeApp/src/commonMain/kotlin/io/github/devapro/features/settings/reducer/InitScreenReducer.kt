package io.github.devapro.features.settings.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.SettingsRepository
import io.github.devapro.features.settings.model.SettingsScreenAction
import io.github.devapro.features.settings.model.SettingsScreenEvent
import io.github.devapro.features.settings.model.SettingsScreenState

/**
 * Reducer for initializing the settings screen by loading current settings values.
 * Loads lock interval, theme mode, and vault file path from the repository.
 */
class InitScreenReducer(
    private val settingsRepository: SettingsRepository
) : Reducer<SettingsScreenAction.InitScreen, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.InitScreen::class

    override suspend fun reduce(
        action: SettingsScreenAction.InitScreen,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent?> {
        return try {
            val lockInterval = settingsRepository.getLockInterval()
            val themeMode = settingsRepository.getThemeMode()
            val vaultFilePath = settingsRepository.getVaultFilePath()

            Reducer.Result(
                state = SettingsScreenState.Success(
                    lockInterval = lockInterval,
                    themeMode = themeMode,
                    vaultFilePath = vaultFilePath
                ),
                action = null,
                event = null
            )
        } catch (e: Exception) {
            Reducer.Result(
                state = SettingsScreenState.Error("Failed to load settings: ${e.message}"),
                action = null,
                event = null
            )
        }
    }
}