package io.github.devapro

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.devapro.di.initKoin
import io.github.vinceglb.filekit.FileKit
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
    Window(
        onCloseRequest = ::exitApplication,
        title = "Droid Passwords",
    ) {
        App()
    }
}