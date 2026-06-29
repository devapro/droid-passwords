package io.github.devapro.droid.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenState

@Composable
fun SettingsScreenContent(
    state: SettingsScreenState.Success,
    onAction: (SettingsScreenAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        SettingSectionHeader(title = "This vault")

        SettingClickableItem(
            title = "Vault name",
            subtitle = state.activeVaultName.ifBlank { "Unnamed" },
            leadingIcon = Icons.Default.Edit,
            onClick = { onAction(SettingsScreenAction.OnRenameVaultClicked) }
        )

        SettingClickableItem(
            title = "Change master password",
            subtitle = "Re-encrypt this vault with a new password",
            leadingIcon = Icons.Default.Lock,
            onClick = { onAction(SettingsScreenAction.OnChangePasswordClicked) }
        )

        SettingClickableItem(
            title = "Remove this vault",
            subtitle = "Remove from the vault list; optionally delete the file",
            leadingIcon = Icons.Default.DeleteForever,
            onClick = { onAction(SettingsScreenAction.OnRemoveVaultClicked) }
        )

        SettingDivider()

        SettingSectionHeader(title = "Security Settings")

        LockIntervalRadioGroup(
            title = "Auto-lock Interval",
            selectedInterval = state.lockInterval,
            onIntervalSelected = { interval ->
                onAction(SettingsScreenAction.OnLockIntervalChanged(interval))
            },
            modifier = Modifier.padding(bottom = 16.dp)
        )

        SettingDivider()

        SettingSectionHeader(title = "Appearance")

        ThemeModeRadioGroup(
            title = "Theme Mode",
            selectedTheme = state.themeMode,
            onThemeSelected = { theme ->
                onAction(SettingsScreenAction.OnThemeModeChanged(theme))
            },
            modifier = Modifier.padding(bottom = 16.dp)
        )

        SettingDivider()

        // Sync Section
        SyncSettingsSection(
            state = state,
            onAction = onAction
        )

        // Dialogs
        ChangePasswordDialog(
            isVisible = state.isChangePasswordDialogVisible,
            isLoading = state.isChangingPassword,
            errorMessage = state.passwordChangeError,
            onDismiss = { onAction(SettingsScreenAction.OnDismissChangePasswordDialog) },
            onPasswordChangeSubmitted = { currentPassword, newPassword, confirmPassword ->
                onAction(
                    SettingsScreenAction.OnPasswordChangeSubmitted(
                        currentPassword = currentPassword,
                        newPassword = newPassword,
                        confirmPassword = confirmPassword
                    )
                )
            }
        )

        RenameVaultDialog(state = state, onAction = onAction)
        RemoveVaultDialog(state = state, onAction = onAction)

        ServerUrlDialog(
            isVisible = state.isServerUrlDialogVisible,
            currentUrl = state.syncServerUrl,
            onDismiss = { onAction(SettingsScreenAction.OnDismissServerUrlDialog) },
            onSubmit = { url -> onAction(SettingsScreenAction.OnServerUrlSubmitted(url)) }
        )

        SyncAuthDialog(
            isVisible = state.isAuthDialogVisible,
            defaultUrl = state.syncServerUrl,
            isLoading = state.isAuthInProgress,
            errorMessage = state.authError,
            onDismiss = { onAction(SettingsScreenAction.OnDismissAuthDialog) },
            onRegister = { url, username, password ->
                onAction(SettingsScreenAction.OnRegisterSubmitted(url, username, password))
            },
            onLogin = { url, username, password ->
                onAction(SettingsScreenAction.OnLoginSubmitted(url, username, password))
            }
        )
    }
}
