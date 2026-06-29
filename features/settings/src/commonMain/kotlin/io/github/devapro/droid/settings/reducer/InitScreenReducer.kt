package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.SettingsRepository
import io.github.devapro.droid.data.sync.SyncStateStore
import io.github.devapro.droid.data.vault.VaultRegistryRepository
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

class InitScreenReducer(
    private val settingsRepository: SettingsRepository,
    private val vaultRegistryRepository: VaultRegistryRepository,
    private val vaultRuntimeRepository: VaultRuntimeRepository,
    private val syncStateStore: SyncStateStore
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
                    vaultFilePath = vaultFilePath,
                    syncServerUrl = syncStateStore.getServerUrl(),
                    syncUsername = syncStateStore.getUsername(),
                    isLoggedIn = syncStateStore.isLoggedIn(),
                    periodicSyncEnabled = syncStateStore.isPeriodicEnabled(),
                    periodicSyncIntervalMinutes = syncStateStore.getPeriodicIntervalMinutes(),
                    lastSyncStatus = syncStateStore.getLastStatus(),
                    lastSyncAt = syncStateStore.getLastSyncAt()
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
