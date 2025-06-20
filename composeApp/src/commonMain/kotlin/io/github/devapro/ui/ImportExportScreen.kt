package io.github.devapro.ui

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
import io.github.devapro.model.ItemModel

enum class FileFormat(val displayName: String, val extension: String) {
    CSV("CSV (Comma Separated Values)", "csv"),
    XML("XML (Extensible Markup Language)", "xml"),
    JSON("JSON (JavaScript Object Notation)", "json")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportExportScreen(
    onBack: () -> Unit,
    onImport: (FileFormat) -> Unit,
    onExport: (FileFormat) -> Unit
) {
    var selectedFormat by remember { mutableStateOf(FileFormat.JSON) }
    var selectedTab by remember { mutableStateOf(0) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Import/Export Passwords") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Import") },
                    icon = { Icon(Icons.Default.Upload, contentDescription = null) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Export") },
                    icon = { Icon(Icons.Default.Download, contentDescription = null) }
                )
            }
            
            // File Format Selection
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
                                        onClick = { selectedFormat = format },
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
                                        text = format.displayName,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            // Format Description
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
                        text = getFormatDescription(selectedFormat),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Action Button
            Button(
                onClick = {
                    if (selectedTab == 0) {
                        onImport(selectedFormat)
                    } else {
                        onExport(selectedFormat)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp)
            ) {
                Icon(
                    imageVector = if (selectedTab == 0) Icons.Default.Upload else Icons.Default.Download,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (selectedTab == 0) "Import from ${selectedFormat.name}" else "Export to ${selectedFormat.name}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            // Warning/Info Text
            Text(
                text = if (selectedTab == 0) {
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

private fun getFormatDescription(format: FileFormat): String {
    return when (format) {
        FileFormat.CSV -> "Comma-separated values format. Compatible with spreadsheet applications like Excel and Google Sheets. Simple and widely supported."
        FileFormat.XML -> "Extensible Markup Language format. Structured data format that preserves field relationships and is human-readable."
        FileFormat.JSON -> "JavaScript Object Notation format. Lightweight, easy to read, and commonly used for data exchange. Recommended for most use cases."
    }
} 