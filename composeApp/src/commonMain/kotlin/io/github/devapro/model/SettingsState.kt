package io.github.devapro.model

/**
 * Data class representing the current state of application settings.
 *
 * @property currentLockInterval The currently selected auto-lock interval
 * @property currentThemeMode The currently selected theme mode
 * @property vaultFilePath The current path where the vault file is stored
 * @property isLoading Whether settings are currently being loaded or saved
 * @property error Error message if any operation failed, null if no error
 */
data class SettingsState(
    val currentLockInterval: LockInterval,
    val currentThemeMode: ThemeMode,
    val vaultFilePath: String,
    val isLoading: Boolean = false,
    val error: String? = null
)