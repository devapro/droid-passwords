package io.github.devapro.droid.core.ui

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.compose.koinInject

/**
 * Reusable SnackbarHost component that uses the app's SnackbarHostStateManager.
 *
 * This component provides:
 * - Automatic integration with SnackbarHostStateManager
 * - Themed snackbar with background color
 * - Auto-dismissal when the composable is disposed
 *
 * Usage:
 * ```
 * Scaffold(
 *     snackbarHost = { AppSnackbarHost() }
 * ) { paddingValues ->
 *     // Your content
 * }
 * ```
 *
 * @param modifier Optional modifier for the SnackbarHost
 * @param autoDismissOnDispose Whether to automatically dismiss the snackbar when the composable is disposed (default: true)
 */
@Composable
fun AppSnackbarHost(
    modifier: Modifier = Modifier
) {
    val snackBarManager: SnackbarHostStateManager = koinInject()

    SnackbarHost(
        modifier = modifier,
        hostState = snackBarManager.state,
        snackbar = { snackbarData ->
            Snackbar(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background),
                snackbarData = snackbarData
            )
        }
    )
}
