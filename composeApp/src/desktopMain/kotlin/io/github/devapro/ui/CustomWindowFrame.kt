package io.github.devapro.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.FrameWindowScope

/**
 * Custom window frame with title bar and window controls.
 * Provides a modern, custom-styled window frame for desktop applications.
 *
 * @param title The window title to display
 * @param content The main content of the window
 */
@Composable
fun FrameWindowScope.CustomWindowFrame(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Custom Title Bar
        WindowDraggableArea(
            modifier = Modifier.fillMaxWidth()
        ) {
            CustomTitleBar(
                title = title,
                windowScope = this@CustomWindowFrame
            )
        }

        // Main Content
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            content()
        }
    }
}
