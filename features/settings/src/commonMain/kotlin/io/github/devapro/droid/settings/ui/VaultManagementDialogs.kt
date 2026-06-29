package io.github.devapro.droid.settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.devapro.droid.core.ui.EOutlinedTextField
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenState

@Composable
fun RenameVaultDialog(
    state: SettingsScreenState.Success,
    onAction: (SettingsScreenAction) -> Unit,
) {
    if (!state.isRenameDialogVisible) return
    AlertDialog(
        onDismissRequest = { onAction(SettingsScreenAction.OnDismissRenameDialog) },
        title = { Text("Rename vault") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                EOutlinedTextField(
                    value = state.renameDraft,
                    onValueChange = { onAction(SettingsScreenAction.OnRenameDraftChanged(it)) },
                    label = { Text("Vault name") },
                    isError = state.renameError != null,
                    supportingText = state.renameError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onAction(SettingsScreenAction.OnRenameVaultSubmitted) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = { onAction(SettingsScreenAction.OnDismissRenameDialog) }) {
                Text("Cancel")
            }
        },
    )
}

@Composable
fun RemoveVaultDialog(
    state: SettingsScreenState.Success,
    onAction: (SettingsScreenAction) -> Unit,
) {
    if (!state.isRemoveDialogVisible) return
    AlertDialog(
        onDismissRequest = { onAction(SettingsScreenAction.OnDismissRemoveDialog) },
        title = { Text("Remove this vault?") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "\"${state.activeVaultName.ifBlank { "Untitled" }}\" will be removed from the vault list.",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = state.removeAlsoDeleteFile,
                        onCheckedChange = { onAction(SettingsScreenAction.OnToggleAlsoDeleteFile) },
                    )
                    Text(
                        text = "Also delete the encrypted file",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                if (!state.removeAlsoDeleteFile) {
                    Text(
                        text = "The file will stay on disk so you can re-add it later.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                state.vaultActionError?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onAction(SettingsScreenAction.OnRemoveVaultConfirmed) }) {
                Text("Remove")
            }
        },
        dismissButton = {
            TextButton(onClick = { onAction(SettingsScreenAction.OnDismissRemoveDialog) }) {
                Text("Cancel")
            }
        },
    )
}
