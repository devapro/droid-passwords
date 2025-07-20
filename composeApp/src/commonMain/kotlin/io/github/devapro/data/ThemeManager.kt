package io.github.devapro.data

import io.github.devapro.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Manager for handling theme preferences and system theme detection.
 * Provides methods to get/set theme mode with Flow-based observation.
 */
class ThemeManager(private val localStorage: LocalStorage) {

    companion object {
        private const val KEY_THEME_MODE = "theme_mode"
        private val DEFAULT_THEME_MODE = ThemeMode.SYSTEM
    }

    /**
     * Sets the theme mode preference.
     * @param mode The theme mode to set
     */
    suspend fun setThemeMode(mode: ThemeMode) {
        localStorage.saveString(KEY_THEME_MODE, mode.name)
    }

    /**
     * Gets the current theme mode as a Flow for observation.
     * @return Flow of ThemeMode that emits when theme preference changes
     */
    fun getThemeMode(): Flow<ThemeMode> {
        return localStorage.getString(KEY_THEME_MODE).map { value ->
            if (value.isEmpty()) {
                DEFAULT_THEME_MODE
            } else {
                try {
                    ThemeMode.valueOf(value)
                } catch (e: IllegalArgumentException) {
                    DEFAULT_THEME_MODE
                }
            }
        }
    }

    /**
     * Gets the current theme mode synchronously.
     * Note: This should be used sparingly as it blocks the current coroutine.
     * @return Current ThemeMode
     */
    suspend fun getCurrentTheme(): ThemeMode {
        return getThemeMode().first()
    }

    /**
     * Determines if the system is currently in dark mode.
     * This is platform-specific and will be implemented in expect/actual pattern.
     * @return true if system is in dark mode, false otherwise
     */
    fun isSystemInDarkMode(): Boolean {
        return getSystemThemeMode()
    }

    /**
     * Gets the effective theme mode considering system theme when SYSTEM mode is selected.
     * @return ThemeMode.DARK or ThemeMode.LIGHT based on current preference and system state
     */
    suspend fun getEffectiveThemeMode(): ThemeMode {
        val currentMode = getCurrentTheme()
        return when (currentMode) {
            ThemeMode.SYSTEM -> if (isSystemInDarkMode()) ThemeMode.DARK else ThemeMode.LIGHT
            else -> currentMode
        }
    }
}

/**
 * Platform-specific system theme detection.
 * Returns true if the system is currently in dark mode.
 */
expect fun getSystemThemeMode(): Boolean