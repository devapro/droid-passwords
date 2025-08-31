package io.github.devapro.droid.export.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.devapro.droid.export.model.ExportScreenAction
import io.github.devapro.droid.export.model.ExportScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportScreenContent(
    state: ExportScreenState,
    onAction: (ExportScreenAction) -> Unit
) {
    LaunchedEffect(Unit) {
        onAction(ExportScreenAction.InitScreen)
    }

    when (state) {
        is ExportScreenState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is ExportScreenState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        is ExportScreenState.Loaded -> {
            ExportLoadedUi(
                state = state,
                onAction = onAction
            )
        }
    }
}