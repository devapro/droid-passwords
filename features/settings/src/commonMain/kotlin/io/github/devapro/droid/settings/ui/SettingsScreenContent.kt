package io.github.devapro.droid.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenState

/**
 * Main settings screen content composable that displays all settings sections.
 *
 * @param state The current settings screen state
 * @param onAction Callback for handling user actions
 * @param modifier Modifier for customizing the layout
 */
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
        // Password Management Section
        SettingSectionHeader(title = "Password Management")

        SettingClickableItem(
            title = "Change Password",
            subtitle = "Update your master password",
            leadingIcon = Icons.Default.Lock,
            onClick = { onAction(SettingsScreenAction.OnChangePasswordClicked) }
        )

        SettingDivider()

        // File Storage Section
        SettingSectionHeader(title = "File Storage")

        SettingClickableItem(
            title = "Vault File Path",
            subtitle = if (state.vaultFilePath.isNotEmpty()) {
                state.vaultFilePath
            } else {
                "Default location (system cache directory)"
            },
            leadingIcon = Icons.Default.Folder,
            onClick = { onAction(SettingsScreenAction.OnFilePathClicked) }
        )

        SettingDivider()

        // Security Settings Section
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

        // Appearance Section
        SettingSectionHeader(title = "Appearance")

        ThemeModeRadioGroup(
            title = "Theme Mode",
            selectedTheme = state.themeMode,
            onThemeSelected = { theme ->
                onAction(SettingsScreenAction.OnThemeModeChanged(theme))
            },
            modifier = Modifier.padding(bottom = 16.dp)
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

        FilePathSelectionDialog(
            isVisible = state.isFilePathDialogVisible,
            currentPath = state.vaultFilePath,
            isLoading = state.isChangingFilePath,
            errorMessage = state.filePathChangeError,
            onDismiss = { onAction(SettingsScreenAction.OnDismissFilePathDialog) },
            onPathSelected = { path ->
                onAction(SettingsScreenAction.OnFilePathSelected(path))
            }
        )
    }
}