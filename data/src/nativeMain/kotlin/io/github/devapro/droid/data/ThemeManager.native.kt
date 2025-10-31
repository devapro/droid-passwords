package io.github.devapro.droid.data

import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle

actual fun getSystemThemeMode(): Boolean {
    return try {
        val traitCollection = UIScreen.mainScreen.traitCollection
        traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark
    } catch (e: Exception) {
        false // Default to light mode on any error
    }
}