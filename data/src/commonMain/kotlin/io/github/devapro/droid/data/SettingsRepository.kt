package io.github.devapro.droid.data

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.data.model.LockInterval
import io.github.devapro.droid.data.model.ThemeMode

/**
 * Repository interface for managing application settings.
 * Provides methods for persisting and retrieving user preferences.
 */
interface SettingsRepository {

    /**
     * Gets the current lock interval setting.
     * @return The currently configured lock interval
     */
    suspend fun getLockInterval(): LockInterval

    /**
     * Sets the lock interval setting.
     * @param interval The lock interval to set
     */
    suspend fun setLockInterval(interval: LockInterval)

    /**
     * Gets the current theme mode setting.
     * @return The currently configured theme mode
     */
    suspend fun getThemeMode(): ThemeMode

    /**
     * Sets the theme mode setting.
     * @param mode The theme mode to set
     */
    suspend fun setThemeMode(mode: ThemeMode)

    /**
     * Gets the current vault file path setting.
     * @return The currently configured vault file path
     */
    suspend fun getVaultFilePath(): String

    /**
     * Sets the vault file path setting.
     * @param path The vault file path to set
     * @return AppResult indicating success or failure with validation
     */
    suspend fun setVaultFilePath(path: String): AppResult<Unit>

    /**
     * Changes the vault password by re-encrypting the vault with a new password.
     * @param oldPassword The current password
     * @param newPassword The new password to set
     * @return AppResult indicating success or failure
     */
    suspend fun changeVaultPassword(oldPassword: String, newPassword: String): AppResult<Unit>
}