package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.SettingsRepository
import io.github.devapro.droid.data.vault.VaultRegistryRepository
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

class InitScreenReducer(
    private val settingsRepository: SettingsRepository,
    private val vaultRegistryRepository: VaultRegistryRepository,
    private val vaultRuntimeRepository: VaultRuntimeRepository,
) : Reducer<SettingsScreenAction.InitScreen, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.InitScreen::class

    override suspend fun reduce(
        action: SettingsScreenAction.InitScreen,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent?> {
        return try {
            val lockInterval = settingsRepository.getLockInterval()
            val themeMode = settingsRepository.getThemeMode()
            val activeName = runCatching { vaultRuntimeRepository.getActiveDescriptor().name }.getOrNull()
                ?: runCatching { vaultRegistryRepository.requireActiveDescriptor().name }.getOrNull()
                ?: ""

            Reducer.Result(
                state = SettingsScreenState.Success(
                    lockInterval = lockInterval,
                    themeMode = themeMode,
                    activeVaultName = activeName,
                ),
                action = null,
                event = null,
            )
        } catch (e: Exception) {
            Reducer.Result(
                state = SettingsScreenState.Error("Failed to load settings: ${e.message}"),
                action = null,
                event = null,
            )
        }
    }
}
