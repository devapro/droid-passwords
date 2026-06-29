package io.github.devapro.droid.data

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.data.model.LockInterval
import io.github.devapro.droid.data.model.ThemeMode
import io.github.devapro.droid.data.vault.VaultFileRepository
import io.github.devapro.droid.data.vault.VaultRegistryRepository
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import kotlinx.coroutines.flow.first

class SettingsRepositoryImpl(
    private val localStorage: LocalStorage,
    private val vaultFileRepository: VaultFileRepository,
    private val vaultRegistryRepository: VaultRegistryRepository,
    private val vaultRuntimeRepository: VaultRuntimeRepository,
) : SettingsRepository {

    companion object {
        private const val KEY_LOCK_INTERVAL = "settings_lock_interval"
        private const val KEY_THEME_MODE = "settings_theme_mode"

        private val DEFAULT_LOCK_INTERVAL = LockInterval.THIRTY_MINUTES
        private val DEFAULT_THEME_MODE = ThemeMode.SYSTEM
    }

    override suspend fun getLockInterval(): LockInterval {
        return try {
            val value = localStorage.getString(KEY_LOCK_INTERVAL).first()
            if (value.isEmpty()) DEFAULT_LOCK_INTERVAL else LockInterval.valueOf(value)
        } catch (_: Exception) {
            DEFAULT_LOCK_INTERVAL
        }
    }

    override suspend fun setLockInterval(interval: LockInterval) {
        localStorage.saveString(KEY_LOCK_INTERVAL, interval.name)
    }

    override suspend fun getThemeMode(): ThemeMode {
        return try {
            val value = localStorage.getString(KEY_THEME_MODE).first()
            if (value.isEmpty()) DEFAULT_THEME_MODE else ThemeMode.valueOf(value)
        } catch (_: Exception) {
            DEFAULT_THEME_MODE
        }
    }

    override suspend fun setThemeMode(mode: ThemeMode) {
        localStorage.saveString(KEY_THEME_MODE, mode.name)
    }

    override suspend fun changeVaultPassword(
        oldPassword: String,
        newPassword: String,
    ): AppResult<Unit> {
        return try {
            if (oldPassword.isEmpty()) {
                return AppResult.Failure(Exception("Current password cannot be empty"))
            }
            if (newPassword.isEmpty()) {
                return AppResult.Failure(Exception("New password cannot be empty"))
            }
            if (oldPassword == newPassword) {
                return AppResult.Failure(Exception("New password must be different from current password"))
            }

            val descriptor = vaultRegistryRepository.requireActiveDescriptor()
            val result = vaultFileRepository.changePassword(descriptor, oldPassword, newPassword)
            when (result) {
                is AppResult.Success -> {
                    vaultRuntimeRepository.replaceActiveVault(result.value)
                    AppResult.Success(Unit)
                }
                is AppResult.Failure -> AppResult.Failure(result.error)
            }
        } catch (e: Exception) {
            AppResult.Failure(Exception("Failed to change vault password: ${e.message}"))
        }
    }
}
