package io.github.devapro

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import io.github.devapro.di.initKoin
import io.github.devapro.droid.data.ThemeManager
import io.github.devapro.droid.data.model.ThemeMode
import io.github.devapro.ui.CustomWindowFrame
import io.github.vinceglb.filekit.FileKit
import org.koin.compose.koinInject
import java.lang.System.setProperty

fun main() = application {
    /**
     * The application ID is used to create user-specific directories on different operating systems:
     *
     * Windows: %APPDATA%\your.application.id\
     * macOS: ~/Library/Application Support/your.application.id/
     * Linux: ~/.local/share/your.application.id/
     */
    setProperty("apple.awt.application.name", "Droid Passwords")
    FileKit.init(appId = "DroidPasswords")
    initKoin()

    val themeManager: ThemeManager = koinInject()
    val themeMode by themeManager.getThemeMode().collectAsState(initial = ThemeMode.SYSTEM)

    val isDarkTheme = when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = if (isDarkTheme) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme
    ) {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Droid Passwords",
            undecorated = true,
            state = WindowState(width = 900.dp, height = 800.dp)
        ) {
            CustomWindowFrame(title = "Droid Passwords") {
                AppContent()
            }
        }
    }
}