package io.github.devapro.droid.data.sync

import io.github.devapro.droid.data.LocalStorage
import kotlinx.coroutines.flow.first

/**
 * Persists device-level sync configuration (server URL, account, auth token and
 * periodic-sync preferences) in the preferences DataStore.
 *
 * Per-item sync state (dirty set, tombstones, last server seq) lives inside the
 * encrypted vault instead, see [io.github.devapro.droid.data.vault.VaultModel].
 */
class SyncStateStore(
    private val localStorage: LocalStorage
) {

    companion object {
        private const val KEY_SERVER_URL = "sync_server_url"
        private const val KEY_USERNAME = "sync_username"
        private const val KEY_TOKEN = "sync_token"
        private const val KEY_PERIODIC_ENABLED = "sync_periodic_enabled"
        private const val KEY_PERIODIC_INTERVAL = "sync_periodic_interval_minutes"
        private const val KEY_LAST_SYNC_AT = "sync_last_sync_at"
        private const val KEY_LAST_STATUS = "sync_last_status"

        const val DEFAULT_INTERVAL_MINUTES = 15
    }

    suspend fun getServerUrl(): String = localStorage.getString(KEY_SERVER_URL).first()

    suspend fun setServerUrl(url: String) = localStorage.saveString(KEY_SERVER_URL, url.trim())

    suspend fun getUsername(): String = localStorage.getString(KEY_USERNAME).first()

    suspend fun getToken(): String = localStorage.getString(KEY_TOKEN).first()

    suspend fun setAccount(username: String, token: String) {
        localStorage.saveString(KEY_USERNAME, username)
        localStorage.saveString(KEY_TOKEN, token)
    }

    suspend fun clearAccount() {
        localStorage.saveString(KEY_USERNAME, "")
        localStorage.saveString(KEY_TOKEN, "")
    }

    suspend fun isLoggedIn(): Boolean =
        getToken().isNotEmpty() && getServerUrl().isNotEmpty()

    suspend fun isPeriodicEnabled(): Boolean =
        localStorage.getString(KEY_PERIODIC_ENABLED).first() == "true"

    suspend fun setPeriodicEnabled(enabled: Boolean) =
        localStorage.saveString(KEY_PERIODIC_ENABLED, if (enabled) "true" else "false")

    suspend fun getPeriodicIntervalMinutes(): Int =
        localStorage.getString(KEY_PERIODIC_INTERVAL).first().toIntOrNull() ?: DEFAULT_INTERVAL_MINUTES

    suspend fun setPeriodicIntervalMinutes(minutes: Int) =
        localStorage.saveString(KEY_PERIODIC_INTERVAL, minutes.toString())

    suspend fun getLastSyncAt(): Long =
        localStorage.getString(KEY_LAST_SYNC_AT).first().toLongOrNull() ?: 0L

    suspend fun getLastStatus(): String = localStorage.getString(KEY_LAST_STATUS).first()

    suspend fun setLastSync(timestamp: Long, status: String) {
        localStorage.saveString(KEY_LAST_SYNC_AT, timestamp.toString())
        localStorage.saveString(KEY_LAST_STATUS, status)
    }
}
