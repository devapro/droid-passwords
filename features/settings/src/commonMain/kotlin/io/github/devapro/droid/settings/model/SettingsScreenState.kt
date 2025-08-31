package io.github.devapro.droid.settings.model

import io.github.devapro.droid.data.model.LockInterval
import io.github.devapro.droid.data.model.ThemeMode

sealed interface SettingsScreenState {
    data object Loading : SettingsScreenState

    data class Error(val message: String) : SettingsScreenState

    data class Success(
        val lockInterval: LockInterval,
        val themeMode: ThemeMode,
        val vaultFilePath: String,
        val isChangePasswordDialogVisible: Boolean = false,
        val isFilePathDialogVisible: Boolean = false,
        val isChangingPassword: Boolean = false,
        val isChangingFilePath: Boolean = false,
        val passwordChangeError: String? = null,
        val filePathChangeError: String? = null
    ) : SettingsScreenState
}