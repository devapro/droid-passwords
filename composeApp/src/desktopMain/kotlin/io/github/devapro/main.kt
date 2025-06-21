package io.github.devapro

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.devapro.di.initKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "Droid Passwords",
    ) {
        App()
    }
}