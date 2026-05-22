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
}
