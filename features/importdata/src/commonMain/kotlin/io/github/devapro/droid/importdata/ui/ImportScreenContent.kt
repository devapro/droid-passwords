package io.github.devapro.droid.importdata.ui

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
import io.github.devapro.droid.importdata.model.ImportScreenAction
import io.github.devapro.droid.importdata.model.ImportScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportScreenContent(
    state: ImportScreenState,
    onAction: (ImportScreenAction) -> Unit
) {
    LaunchedEffect(Unit) {
        onAction(ImportScreenAction.InitScreen)
    }

    when (state) {
        is ImportScreenState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is ImportScreenState.Error -> {
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

        is ImportScreenState.Loaded -> {
            ImportLoadedUi(
                state = state,
                onAction = onAction
            )
        }
    }
}