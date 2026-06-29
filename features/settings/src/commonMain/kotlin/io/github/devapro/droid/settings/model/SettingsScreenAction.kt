package io.github.devapro.droid.settings.model

import io.github.devapro.droid.data.model.LockInterval
import io.github.devapro.droid.data.model.ThemeMode

sealed interface SettingsScreenAction {
    data object InitScreen : SettingsScreenAction

    data object OnBackClicked : SettingsScreenAction

    data object OnChangePasswordClicked : SettingsScreenAction

    data class OnLockIntervalChanged(val interval: LockInterval) : SettingsScreenAction

    data class OnThemeModeChanged(val mode: ThemeMode) : SettingsScreenAction

    data class OnPasswordChangeSubmitted(
        val currentPassword: String,
        val newPassword: String,
        val confirmPassword: String,
    ) : SettingsScreenAction

    data object OnDismissChangePasswordDialog : SettingsScreenAction

    data object OnRenameVaultClicked : SettingsScreenAction
    data class OnRenameDraftChanged(val name: String) : SettingsScreenAction
    data object OnRenameVaultSubmitted : SettingsScreenAction
    data object OnDismissRenameDialog : SettingsScreenAction

    data object OnRemoveVaultClicked : SettingsScreenAction
    data object OnToggleAlsoDeleteFile : SettingsScreenAction
    data object OnRemoveVaultConfirmed : SettingsScreenAction
    data object OnDismissRemoveDialog : SettingsScreenAction

    // --- Sync ---
    data object OnServerUrlClicked : SettingsScreenAction

    data class OnServerUrlSubmitted(val url: String) : SettingsScreenAction

    data object OnDismissServerUrlDialog : SettingsScreenAction

    data object OnLoginClicked : SettingsScreenAction

    data object OnDismissAuthDialog : SettingsScreenAction

    data class OnRegisterSubmitted(
        val url: String,
        val username: String,
        val password: String
    ) : SettingsScreenAction

    data class OnLoginSubmitted(
        val url: String,
        val username: String,
        val password: String
    ) : SettingsScreenAction

    data object OnLogoutClicked : SettingsScreenAction

    data object OnSyncNowClicked : SettingsScreenAction

    data object OnSyncNowExecute : SettingsScreenAction

    data object OnSyncToServerClicked : SettingsScreenAction

    data object OnSyncToServerExecute : SettingsScreenAction

    data object OnSyncFromServerClicked : SettingsScreenAction

    data object OnSyncFromServerExecute : SettingsScreenAction

    data class OnPeriodicSyncToggled(val enabled: Boolean) : SettingsScreenAction

    data class OnPeriodicIntervalChanged(val minutes: Int) : SettingsScreenAction
}
