package io.github.devapro.data

import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle

/**
 * iOS-specific implementation for system theme detection.
 * Uses UITraitCollection to determine if dark mode is active.
 */
actual fun getSystemThemeMode(): Boolean {
    return try {
        val traitCollection = UIScreen.mainScreen.traitCollection
        traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark
    } catch (e: Exception) {
        false // Default to light mode on any error
    }
}