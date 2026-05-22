package io.github.devapro.droid.data

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.data.model.LockInterval
import io.github.devapro.droid.data.model.ThemeMode

interface SettingsRepository {
    suspend fun getLockInterval(): LockInterval
    suspend fun setLockInterval(interval: LockInterval)
    suspend fun getThemeMode(): ThemeMode
    suspend fun setThemeMode(mode: ThemeMode)
    suspend fun changeVaultPassword(oldPassword: String, newPassword: String): AppResult<Unit>
}
