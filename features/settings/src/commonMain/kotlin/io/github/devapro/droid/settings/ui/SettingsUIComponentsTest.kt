package io.github.devapro.droid.settings.ui

import androidx.compose.runtime.Composable
import io.github.devapro.droid.data.model.LockInterval
import io.github.devapro.droid.data.model.ThemeMode

/**
 * Simple test composable to verify all UI components compile correctly.
 * This is not a unit test but a compilation verification.
 */
@Composable
private fun TestAllComponents() {
    // Test SettingItem
    SettingItem(
        title = "Test Setting",
        subtitle = "Test subtitle"
    )

    // Test SettingClickableItem
    SettingClickableItem(
        title = "Test Clickable",
        subtitle = "Test subtitle",
        onClick = {}
    )

    // Test SettingRadioGroup
    SettingRadioGroup(
        title = "Test Radio Group",
        options = listOf("Option 1", "Option 2"),
        selectedOption = "Option 1",
        onOptionSelected = {},
        getDisplayName = { it }
    )

    // Test LockIntervalRadioGroup
    LockIntervalRadioGroup(
        title = "Lock Interval",
        selectedInterval = LockInterval.FIFTEEN_MINUTES,
        onIntervalSelected = {}
    )

    // Test ThemeModeRadioGroup
    ThemeModeRadioGroup(
        title = "Theme Mode",
        selectedTheme = ThemeMode.SYSTEM,
        onThemeSelected = {}
    )

    // Test SettingSectionHeader
    SettingSectionHeader(title = "Test Section")

    // Test SettingDivider
    SettingDivider()

    // Test ChangePasswordDialog
    ChangePasswordDialog(
        isVisible = true,
        isLoading = false,
        errorMessage = null,
        onDismiss = {},
        onPasswordChangeSubmitted = { _, _, _ -> }
    )

    // Test FilePathSelectionDialog
    FilePathSelectionDialog(
        isVisible = true,
        currentPath = "/current/path",
        isLoading = false,
        errorMessage = null,
        onDismiss = {},
        onPathSelected = {}
    )
}