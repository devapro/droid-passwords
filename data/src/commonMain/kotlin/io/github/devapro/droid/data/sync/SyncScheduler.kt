package io.github.devapro.droid.data.sync

import io.github.devapro.droid.core.mvi.CoroutineContextProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

/**
 * Runs [SyncManager.syncNow] on a fixed interval while the app is running and the
 * vault is unlocked. This provides the "periodic sync" behaviour: changes made on
 * one device propagate to others on the next scheduled run.
 *
 * Note: this scheduler only runs while the process is alive. For OS-level
 * background sync on Android, a WorkManager worker could call [SyncManager.syncNow]
 * as a future enhancement.
 */
class SyncScheduler(
    private val syncManager: SyncManager,
    private val syncStateStore: SyncStateStore,
    private val coroutineContextProvider: CoroutineContextProvider
) {

    private val scope = coroutineContextProvider.createScope(coroutineContextProvider.default)
    private var job: Job? = null

    /** Starts periodic sync if it has been enabled in settings. Safe to call repeatedly. */
    fun startIfEnabled() {
        scope.launch {
            if (syncStateStore.isPeriodicEnabled() && syncStateStore.isLoggedIn()) {
                start(syncStateStore.getPeriodicIntervalMinutes())
            }
        }
    }

    fun start(intervalMinutes: Int) {
        stop()
        val interval = intervalMinutes.coerceAtLeast(1).minutes
        job = scope.launch {
            while (isActive) {
                delay(interval)
                if (syncStateStore.isLoggedIn()) {
                    runCatching { syncManager.syncNow() }
                }
            }
        }
    }

    fun stop() {
        job?.cancel()
        job = null
    }
}
