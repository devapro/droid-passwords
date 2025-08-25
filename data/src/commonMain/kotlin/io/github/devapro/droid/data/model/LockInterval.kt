package io.github.devapro.droid.data.model

/**
 * Enum representing different auto-lock interval options for the application.
 *
 * @property minutes The number of minutes for the lock interval (0 means never lock)
 * @property displayName The human-readable name for display in UI
 */
enum class LockInterval(val minutes: Int, val displayName: String) {
    FIFTEEN_MINUTES(15, "15 minutes"),
    THIRTY_MINUTES(30, "30 minutes"),
    NEVER(0, "Never")
}