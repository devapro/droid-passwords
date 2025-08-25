package io.github.devapro.droid.data.model

/**
 * Enum representing different theme mode options for the application.
 *
 * @property displayName The human-readable name for display in UI
 */
enum class ThemeMode(val displayName: String) {
    LIGHT("Light"),
    DARK("Dark"),
    SYSTEM("System")
}