package io.github.devapro.features.settings.model

import io.github.devapro.droid.data.model.LockInterval
import io.github.devapro.droid.data.model.ThemeMode

sealed interface SettingsScreenAction {
    data object InitScreen : SettingsScreenAction

    data object OnBackClicked : SettingsScreenAction

    data object OnChangePasswordClicked : SettingsScreenAction

    data object OnFilePathClicked : SettingsScreenAction

    data class OnLockIntervalChanged(val interval: LockInterval) : SettingsScreenAction

    data class OnThemeModeChanged(val mode: ThemeMode) : SettingsScreenAction

    data class OnPasswordChangeSubmitted(
        val currentPassword: String,
        val newPassword: String,
        val confirmPassword: String
    ) : SettingsScreenAction

    data class OnFilePathSelected(val path: String) : SettingsScreenAction

    data object OnDismissChangePasswordDialog : SettingsScreenAction

    data object OnDismissFilePathDialog : SettingsScreenAction
}