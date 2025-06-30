package io.github.devapro.ui.importexport.ui

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
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.devapro.ui.importexport.model.ImportExportScreenAction
import io.github.devapro.ui.importexport.model.ImportExportScreenState
import io.github.devapro.ui.importexport.model.ImportExportTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportExportLoadedUi(
    state: ImportExportScreenState.Loaded,
    onAction: (ImportExportScreenAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Import/Export Passwords") },
                navigationIcon = {
                    IconButton(onClick = { onAction(ImportExportScreenAction.OnBackClicked) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Tab Row for Import/Export
            TabRow(selectedTabIndex = if (state.selectedTab == ImportExportTab.IMPORT) 0 else 1) {
                Tab(
                    selected = state.selectedTab == ImportExportTab.IMPORT,
                    onClick = { onAction(ImportExportScreenAction.OnSwitchToImport) },
                    text = { Text("Import") },
                    icon = { Icon(Icons.Default.Upload, contentDescription = null) }
                )
                Tab(
                    selected = state.selectedTab == ImportExportTab.EXPORT,
                    onClick = { onAction(ImportExportScreenAction.OnSwitchToExport) },
                    text = { Text("Export") },
                    icon = { Icon(Icons.Default.Download, contentDescription = null) }
                )
            }

            // File Format Selection
            FormatSelectionCard(
                state = state,
                onFormatSelected = { format ->
                    onAction(ImportExportScreenAction.OnFormatSelected(format))
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Action Button
            Button(
                onClick = {
                    if (state.selectedTab == ImportExportTab.IMPORT) {
                        onAction(ImportExportScreenAction.OnImportClicked)
                    } else {
                        onAction(ImportExportScreenAction.OnExportClicked)
                    }
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
                        imageVector = if (state.selectedTab == ImportExportTab.IMPORT)
                            Icons.Default.Upload else Icons.Default.Download,
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (state.selectedTab == ImportExportTab.IMPORT)
                        "Import from ${state.selectedFormat.name}"
                    else
                        "Export to ${state.selectedFormat.name}",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Warning/Info Text
            Text(
                text = if (state.selectedTab == ImportExportTab.IMPORT) {
                    "⚠️ Import will add new passwords to your existing collection. Duplicates may be created."
                } else {
                    "💾 Export will save all your passwords to a file. Keep this file secure!"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}