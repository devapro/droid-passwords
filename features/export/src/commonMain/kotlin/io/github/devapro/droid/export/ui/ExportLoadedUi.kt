package io.github.devapro.droid.export.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.devapro.droid.export.model.ExportScreenAction
import io.github.devapro.droid.export.model.ExportScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportLoadedUi(
    state: ExportScreenState.Loaded,
    onAction: (ExportScreenAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Export Passwords") },
                navigationIcon = {
                    IconButton(onClick = { onAction(ExportScreenAction.OnBackClicked) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // File Format Selection
            FormatSelectionCard(
                state = state,
                onFormatSelected = { format ->
                    onAction(ExportScreenAction.OnFormatSelected(format))
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Action Button
            Button(
                onClick = {
                    onAction(ExportScreenAction.OnExportClicked)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isProcessing,
                contentPadding = PaddingValues(16.dp)
            ) {
                if (state.isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Export to ${state.selectedFormat.name}",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Warning/Info Text
            Text(
                text = "💾 Export will save all your passwords to a file. Keep this file secure!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}