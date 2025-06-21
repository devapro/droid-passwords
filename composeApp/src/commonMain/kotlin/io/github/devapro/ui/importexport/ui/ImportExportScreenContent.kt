package io.github.devapro.ui.importexport.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.devapro.ui.importexport.model.ImportExportScreenAction
import io.github.devapro.ui.importexport.model.ImportExportScreenState
import io.github.devapro.ui.importexport.model.ImportExportTab
import io.github.devapro.ui.importexport.model.FileFormat

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
        
        is ImportExportScreenState.Success -> {
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
                        selectedFormat = state.selectedFormat,
                        formatDescription = state.formatDescription,
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
    }
}

@Composable
private fun FormatSelectionCard(
    selectedFormat: FileFormat,
    formatDescription: String,
    onFormatSelected: (FileFormat) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Select File Format",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Column(modifier = Modifier.selectableGroup()) {
                FileFormat.values().forEach { format ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (selectedFormat == format),
                                onClick = { onFormatSelected(format) },
                                role = Role.RadioButton
                            )
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (selectedFormat == format),
                            onClick = null
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = format.name,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = getFormatDisplayName(format),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            
            // Format Description Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Format Information",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = formatDescription,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}

private fun getFormatDisplayName(format: FileFormat): String {
    return when (format) {
        FileFormat.CSV -> "CSV (Comma Separated Values)"
        FileFormat.XML -> "XML (Extensible Markup Language)"
        FileFormat.JSON -> "JSON (JavaScript Object Notation)"
    }
} 