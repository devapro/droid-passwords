package io.github.devapro.features.settings.ui

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
import io.github.devapro.model.LockInterval
import io.github.devapro.model.ThemeMode

/**
 * Example usage of the settings UI components.
 * This demonstrates how to compose a complete settings screen using the created components.
 */
@Composable
fun SettingsScreenContentExample(
    currentLockInterval: LockInterval = LockInterval.FIFTEEN_MINUTES,
    currentThemeMode: ThemeMode = ThemeMode.SYSTEM,
    vaultFilePath: String = "/default/path/vault.dat",
    onLockIntervalChanged: (LockInterval) -> Unit = {},
    onThemeModeChanged: (ThemeMode) -> Unit = {},
    onChangePasswordClicked: () -> Unit = {},
    onFilePathClicked: () -> Unit = {},
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
            onClick = onChangePasswordClicked
        )

        SettingDivider()

        // File Storage Section
        SettingSectionHeader(title = "File Storage")

        SettingClickableItem(
            title = "Vault File Path",
            subtitle = vaultFilePath,
            leadingIcon = Icons.Default.Folder,
            onClick = onFilePathClicked
        )

        SettingDivider()

        // Security Settings Section
        SettingSectionHeader(title = "Security Settings")

        LockIntervalRadioGroup(
            title = "Auto-lock Interval",
            selectedInterval = currentLockInterval,
            onIntervalSelected = onLockIntervalChanged,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        SettingDivider()

        // Appearance Section
        SettingSectionHeader(title = "Appearance")

        ThemeModeRadioGroup(
            title = "Theme Mode",
            selectedTheme = currentThemeMode,
            onThemeSelected = onThemeModeChanged,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Example of basic setting item (read-only)
        SettingDivider()
        SettingSectionHeader(title = "Information")

        SettingItem(
            title = "App Version",
            subtitle = "1.0.0"
        )
    }
}