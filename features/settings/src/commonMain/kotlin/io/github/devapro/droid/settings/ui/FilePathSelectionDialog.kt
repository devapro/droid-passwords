package io.github.devapro.droid.settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.openDirectoryPicker
import kotlinx.coroutines.launch

/**
 * Dialog for selecting a new vault file path using FileKit directory picker.
 *
 * @param isVisible Whether the dialog is visible
 * @param currentPath The current vault file path
 * @param isLoading Whether the path change operation is in progress
 * @param errorMessage Error message to display, null if no error
 * @param onDismiss Callback when the dialog is dismissed
 * @param onPathSelected Callback when a new path is selected
 */
@Composable
fun FilePathSelectionDialog(
    isVisible: Boolean,
    currentPath: String,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onDismiss: () -> Unit,
    onPathSelected: (String) -> Unit
) {
    if (!isVisible) return

    var selectedPath by remember(isVisible) { mutableStateOf("") }
    var showConfirmation by remember { mutableStateOf(false) }
    var pathValidationError by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Reset state when dialog becomes visible
    LaunchedEffect(isVisible) {
        if (isVisible) {
            selectedPath = currentPath
            showConfirmation = false
            pathValidationError = null
        }
    }

    // Validate path whenever it changes
    LaunchedEffect(selectedPath) {
        pathValidationError = validatePath(selectedPath, currentPath)
    }

    if (showConfirmation && selectedPath != currentPath) {
        // Confirmation dialog for path changes
        ConfirmationDialog(
            currentPath = currentPath,
            newPath = selectedPath,
            onConfirm = {
                showConfirmation = false
                onPathSelected(selectedPath)
            },
            onCancel = {
                showConfirmation = false
            }
        )
    } else {
        // Main file path selection dialog
        AlertDialog(
            onDismissRequest = {
                if (!isLoading) {
                    onDismiss()
                }
            },
            title = {
                Text(
                    text = "Change Vault File Path",
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Current path display
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Current Path:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = currentPath.ifEmpty { "Default location (system cache directory)" },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Path input field with validation
                    OutlinedTextField(
                        value = selectedPath,
                        onValueChange = { selectedPath = it },
                        label = { Text("New Directory Path") },
                        placeholder = { Text("Enter or browse for directory path") },
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = pathValidationError != null,
                        supportingText = pathValidationError?.let { error ->
                            {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.width(16.dp).height(16.dp)
                                    )
                                    Text(
                                        text = error,
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    )

                    // Browse button with FileKit integration
                    OutlinedButton(
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    val directory = FileKit.openDirectoryPicker()
                                    directory?.let { dir ->
                                        // TODO: Find the correct property to get path from PlatformFile
                                        // For now, use toString() as a fallback
                                        selectedPath = dir.toString()
                                    }
                                } catch (e: Exception) {
                                    // Handle FileKit errors gracefully
                                    pathValidationError =
                                        "Failed to open directory picker: ${e.message}"
                                }
                            }
                        },
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Browse for Directory")
                    }

                    // Path validation and info
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "ℹ️ Important Information",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "• The selected directory will store your encrypted vault file\n" +
                                        "• Ensure you have write permissions to the location\n" +
                                        "• Existing vault data will be safely migrated to the new location\n" +
                                        "• Choose a location you can easily remember and access",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Server error message
                    errorMessage?.let { error ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                                Text(
                                    text = error,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.width(16.dp).height(16.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    TextButton(
                        onClick = {
                            if (!isLoading && selectedPath.isNotEmpty() && pathValidationError == null) {
                                if (selectedPath != currentPath) {
                                    showConfirmation = true
                                } else {
                                    onDismiss() // No change needed
                                }
                            }
                        },
                        enabled = selectedPath.isNotEmpty() && !isLoading && pathValidationError == null
                    ) {
                        Text(
                            if (isLoading) "Updating..."
                            else if (selectedPath == currentPath) "No Change"
                            else "Update Path"
                        )
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss,
                    enabled = !isLoading
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

/**
 * Validates the selected file path.
 * @param path The file path to validate
 * @param currentPath The current path for comparison
 * @return Error message if validation fails, null if validation passes
 */
private fun validatePath(path: String, currentPath: String): String? {
    return when {
        path.isEmpty() -> null // Empty is allowed (will use default)
        path == currentPath -> null // Same as current is valid
        path.length > 500 -> "File path is too long (maximum 500 characters)"
        path.contains("..") -> "File path cannot contain relative path components (..)"
        path.trim() != path -> "File path cannot have leading or trailing spaces"
        // Check for invalid characters (basic validation)
        path.contains(Regex("[<>:\"|?*]")) -> "File path contains invalid characters"
        else -> null
    }
}

/**
 * Confirmation dialog shown when user wants to change the vault file path.
 * Provides clear information about what will happen during the path change.
 */
@Composable
private fun ConfirmationDialog(
    currentPath: String,
    newPath: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Text(
                text = "Confirm Path Change",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "You are about to change your vault file location. This operation will:",
                    style = MaterialTheme.typography.bodyMedium
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Migration Process:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "1. Safely copy your encrypted vault to the new location\n" +
                                    "2. Verify the integrity of the copied data\n" +
                                    "3. Update the application settings\n" +
                                    "4. Remove the old vault file (if migration succeeds)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Path comparison
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "From:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = currentPath.ifEmpty { "Default location" },
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = "To:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(
                            text = newPath,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = "Make sure the new location is accessible and you have write permissions. " +
                                    "If the migration fails, your data will remain in the current location.",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text("Proceed with Migration")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCancel
            ) {
                Text("Cancel")
            }
        }
    )
}