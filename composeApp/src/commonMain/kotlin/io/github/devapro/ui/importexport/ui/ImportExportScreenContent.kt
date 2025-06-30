package io.github.devapro.ui.importexport.ui

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
import io.github.devapro.ui.importexport.model.ImportExportScreenAction
import io.github.devapro.ui.importexport.model.ImportExportScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportExportScreenContent(
    state: ImportExportScreenState,
    onAction: (ImportExportScreenAction) -> Unit
) {
    LaunchedEffect(Unit) {
        onAction(ImportExportScreenAction.InitScreen)
    }

    when (state) {
        is ImportExportScreenState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        
        is ImportExportScreenState.Error -> {
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

        is ImportExportScreenState.Loaded -> {
            ImportExportLoadedUi(
                state = state,
                onAction = onAction
            )
        }
    }
}