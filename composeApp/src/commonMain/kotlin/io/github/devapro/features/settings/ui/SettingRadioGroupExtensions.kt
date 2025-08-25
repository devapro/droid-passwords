package io.github.devapro.features.settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.devapro.droid.data.model.LockInterval
import io.github.devapro.droid.data.model.ThemeMode

/**
 * Convenience composable for LockInterval radio group.
 */
@Composable
fun LockIntervalRadioGroup(
    title: String,
    selectedInterval: LockInterval,
    onIntervalSelected: (LockInterval) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingRadioGroup(
        title = title,
        options = LockInterval.entries,
        selectedOption = selectedInterval,
        onOptionSelected = onIntervalSelected,
        getDisplayName = { it.displayName },
        modifier = modifier
    )
}

/**
 * Convenience composable for ThemeMode radio group.
 */
@Composable
fun ThemeModeRadioGroup(
    title: String,
    selectedTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingRadioGroup(
        title = title,
        options = ThemeMode.entries,
        selectedOption = selectedTheme,
        onOptionSelected = onThemeSelected,
        getDisplayName = { it.displayName },
        modifier = modifier
    )
}