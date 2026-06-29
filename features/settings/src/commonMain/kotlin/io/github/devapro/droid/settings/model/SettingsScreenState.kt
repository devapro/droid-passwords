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
        val filePathChangeError: String? = null,
        // Sync
        val syncServerUrl: String = "",
        val syncUsername: String = "",
        val isLoggedIn: Boolean = false,
        val periodicSyncEnabled: Boolean = false,
        val periodicSyncIntervalMinutes: Int = 15,
        val lastSyncStatus: String = "",
        val lastSyncAt: Long = 0,
        val isSyncing: Boolean = false,
        val isServerUrlDialogVisible: Boolean = false,
        val isAuthDialogVisible: Boolean = false,
        val isAuthInProgress: Boolean = false,
        val authError: String? = null,
        // Master-password prompt for server vaults encrypted with a different password
        val isLinkMasterPasswordDialogVisible: Boolean = false,
        val isLinkInProgress: Boolean = false,
        val linkMasterPasswordError: String? = null
    ) : SettingsScreenState
}
