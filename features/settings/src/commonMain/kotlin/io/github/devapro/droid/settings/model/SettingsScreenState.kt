package io.github.devapro.droid.settings.model

import io.github.devapro.droid.data.model.LockInterval
import io.github.devapro.droid.data.model.ThemeMode

sealed interface SettingsScreenState {
    data object Loading : SettingsScreenState

    data class Error(val message: String) : SettingsScreenState

    data class Success(
        val lockInterval: LockInterval,
        val themeMode: ThemeMode,
        val activeVaultName: String,
        val isChangePasswordDialogVisible: Boolean = false,
        val isChangingPassword: Boolean = false,
        val passwordChangeError: String? = null,
        val isRenameDialogVisible: Boolean = false,
        val renameDraft: String = "",
        val renameError: String? = null,
        val isRemoveDialogVisible: Boolean = false,
        val removeAlsoDeleteFile: Boolean = false,
        val isPerformingVaultAction: Boolean = false,
        val vaultActionError: String? = null,
    ) : SettingsScreenState
}
