package io.github.devapro.droid.edit.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import io.github.devapro.droid.edit.model.AddEditPasswordScreenAction

@Composable
fun DeleteConfirmationDialog(
    onAction: (AddEditPasswordScreenAction) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onAction(AddEditPasswordScreenAction.OnDeleteCancelled) },
        title = { Text("Delete Password") },
        text = { Text("Are you sure you want to delete this password? This action cannot be undone.") },
        icon = { Icon(Icons.Default.Delete, contentDescription = "Delete") },
        confirmButton = {
            TextButton(
                onClick = { onAction(AddEditPasswordScreenAction.OnDeleteConfirmed) }
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onAction(AddEditPasswordScreenAction.OnDeleteCancelled) }
            ) {
                Text("Cancel")
            }
        }
    )
}
