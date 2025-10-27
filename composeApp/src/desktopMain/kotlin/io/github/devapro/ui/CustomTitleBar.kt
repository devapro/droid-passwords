package io.github.devapro.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.FilterNone
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.WindowPlacement
import java.awt.Frame

/**
 * Custom title bar for the window with title and window controls.
 *
 * @param title The window title
 * @param windowScope The window scope for accessing window state
 */
@Composable
fun CustomTitleBar(
    title: String,
    windowScope: FrameWindowScope
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(MaterialTheme.colorScheme.surfaceContainer),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Title
        Text(
            text = title,
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // Window Controls
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Minimize Button
            WindowControlButton(
                icon = Icons.Default.Minimize,
                contentDescription = "Minimize",
                onClick = {
                    windowScope.window.extendedState = Frame.ICONIFIED
                }
            )

            // Maximize/Restore Button
            val isMaximized = windowScope.window.placement == WindowPlacement.Maximized
            WindowControlButton(
                icon = if (isMaximized) Icons.Default.FilterNone else Icons.Default.CropSquare,
                contentDescription = if (isMaximized) "Restore" else "Maximize",
                onClick = {
                    windowScope.window.placement = if (isMaximized) {
                        WindowPlacement.Floating
                    } else {
                        WindowPlacement.Maximized
                    }
                }
            )

            // Close Button
            WindowControlButton(
                icon = Icons.Default.Close,
                contentDescription = "Close",
                isCloseButton = true,
                onClick = {
                    windowScope.window.dispatchEvent(
                        java.awt.event.WindowEvent(
                            windowScope.window,
                            java.awt.event.WindowEvent.WINDOW_CLOSING
                        )
                    )
                }
            )
        }
    }
}

/**
 * Window control button (minimize, maximize, close).
 */
@Composable
private fun WindowControlButton(
    icon: ImageVector,
    contentDescription: String,
    isCloseButton: Boolean = false,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val backgroundColor = when {
        isCloseButton && isHovered -> Color(0xFFE81123)
        isHovered -> MaterialTheme.colorScheme.surfaceVariant
        else -> Color.Transparent
    }

    val iconColor = when {
        isCloseButton && isHovered -> Color.White
        else -> MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = Modifier
            .width(48.dp)
            .height(48.dp)
            .background(backgroundColor)
            .hoverable(interactionSource),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(48.dp),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = iconColor
            )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
