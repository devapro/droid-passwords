package io.github.devapro.data

import android.app.Application
import android.content.res.Configuration

/**
 * Android-specific implementation for system theme detection.
 * Uses the system's UI mode to determine if dark theme is active.
 */
actual fun getSystemThemeMode(): Boolean {
    return try {
        // Get the application context through a static reference
        // This is a simplified approach - in production, you might want to inject context
        val context = AndroidThemeDetector.applicationContext
        if (context != null) {
            val uiMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            uiMode == Configuration.UI_MODE_NIGHT_YES
        } else {
            false // Default to light mode if context is not available
        }
    } catch (e: Exception) {
        false // Default to light mode on any error
    }
}

/**
 * Helper object to store application context for theme detection.
 * This should be initialized in the Application class.
 */
object AndroidThemeDetector {
    var applicationContext: Application? = null
        private set

    fun initialize(application: Application) {
        applicationContext = application
    }
}