package io.github.devapro.droid.importdata.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import io.github.devapro.droid.core.ui.EOutlinedTextField
import io.github.devapro.droid.importdata.model.FileFormat
import io.github.devapro.droid.importdata.model.ImportScreenAction
import io.github.devapro.droid.importdata.model.ImportScreenState
import io.github.devapro.droid.importdata.model.ImportStrategy
import io.github.devapro.droid.importdata.model.ImportTarget

@Composable
fun ImportConfirmDialog(
    state: ImportScreenState.Loaded,
    onAction: (ImportScreenAction) -> Unit,
) {
    val parsed = state.parsedItems ?: return
    val canMerge = state.canMergeIntoActive
    val report = state.conflictReport
    val needsPasswordForNewVault = state.target == ImportTarget.NEW_VAULT
        && state.selectedFormat != FileFormat.DATA

    AlertDialog(
        onDismissRequest = {
            if (!state.isProcessing) {
                onAction(ImportScreenAction.OnDismissConfirmDialog)
            }
        },
        title = { Text("Review import") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "${parsed.size} item(s) parsed from ${state.selectedFormat.name}.",
                    style = MaterialTheme.typography.bodyMedium,
                )

                if (canMerge) {
                    SectionTitle("Where should they go?")
                    TargetRadio(
                        selected = state.target,
                        onSelect = { onAction(ImportScreenAction.OnTargetSelected(it)) },
                        activeVaultName = state.activeVaultName,
                    )
                }

                when (state.target) {
                    ImportTarget.MERGE_INTO_ACTIVE -> {
                        if (report != null) {
                            Text(
                                text = "${report.freshCount} new, ${report.matchedCount} match existing (title + username).",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            if (report.matched.isNotEmpty()) {
                                LazyColumn(
                                    modifier = Modifier.heightIn(max = 120.dp),
                                    verticalArrangement = Arrangement.spacedBy(2.dp),
                                ) {
                                    items(report.matched, key = { it.id }) { match ->
                                        Text(
                                            text = "• ${match.title.ifBlank { "(no title)" }} — ${match.username}",
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.padding(start = 4.dp),
                                        )
                                    }
                                }
                            }
                        }
                        SectionTitle("Strategy")
                        StrategyRadio(
                            selected = state.strategy,
                            onSelect = { onAction(ImportScreenAction.OnStrategySelected(it)) },
                        )
                    }

                    ImportTarget.NEW_VAULT -> {
                        SectionTitle("New vault")
                        EOutlinedTextField(
                            value = state.newVaultName,
                            onValueChange = { onAction(ImportScreenAction.OnNewVaultNameChanged(it)) },
                            label = { Text("Vault name") },
                            placeholder = { Text("Imported vault") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                        )
                        if (needsPasswordForNewVault) {
                            EOutlinedTextField(
                                value = state.password,
                                onValueChange = { onAction(ImportScreenAction.OnPasswordChanged(it)) },
                                label = { Text("Master password") },
                                placeholder = { Text("Encrypts the new vault file") },
                                visualTransformation = if (state.isPasswordVisible) {
                                    VisualTransformation.None
                                } else {
                                    PasswordVisualTransformation()
                                },
                                trailingIcon = {
                                    IconButton(onClick = { onAction(ImportScreenAction.OnTogglePasswordVisibility) }) {
                                        Icon(
                                            imageVector = if (state.isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = null,
                                        )
                                    }
                                },
                                isError = state.passwordError != null,
                                supportingText = state.passwordError?.let { { Text(it) } },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                            )
                        } else {
                            Text(
                                text = "The new vault will be encrypted with the password you entered to decrypt the source file.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onAction(ImportScreenAction.OnConfirmImport) },
                enabled = !state.isProcessing,
            ) { Text(confirmLabel(state)) }
        },
        dismissButton = {
            TextButton(
                onClick = { onAction(ImportScreenAction.OnDismissConfirmDialog) },
                enabled = !state.isProcessing,
            ) { Text("Cancel") }
        },
    )
}

private fun confirmLabel(state: ImportScreenState.Loaded): String = when (state.target) {
    ImportTarget.NEW_VAULT -> "Create vault"
    ImportTarget.MERGE_INTO_ACTIVE -> when (state.strategy) {
        ImportStrategy.REPLACE -> "Replace"
        ImportStrategy.MERGE_BY_TITLE_USERNAME -> "Merge"
        ImportStrategy.APPEND -> "Append"
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
    )
}

@Composable
private fun TargetRadio(
    selected: ImportTarget,
    onSelect: (ImportTarget) -> Unit,
    activeVaultName: String,
) {
    Column(modifier = Modifier.selectableGroup()) {
        RadioRow(
            label = "Add as new vault",
            checked = selected == ImportTarget.NEW_VAULT,
            onClick = { onSelect(ImportTarget.NEW_VAULT) },
        )
        RadioRow(
            label = if (activeVaultName.isNotBlank()) "Merge into \"$activeVaultName\"" else "Merge into active vault",
            checked = selected == ImportTarget.MERGE_INTO_ACTIVE,
            onClick = { onSelect(ImportTarget.MERGE_INTO_ACTIVE) },
        )
    }
}

@Composable
private fun StrategyRadio(
    selected: ImportStrategy,
    onSelect: (ImportStrategy) -> Unit,
) {
    Column(modifier = Modifier.selectableGroup()) {
        RadioRow(
            label = "Merge by title + username (skip duplicates)",
            checked = selected == ImportStrategy.MERGE_BY_TITLE_USERNAME,
            onClick = { onSelect(ImportStrategy.MERGE_BY_TITLE_USERNAME) },
        )
        RadioRow(
            label = "Append (add everything, allow duplicates)",
            checked = selected == ImportStrategy.APPEND,
            onClick = { onSelect(ImportStrategy.APPEND) },
        )
        RadioRow(
            label = "Replace (wipe active items first)",
            checked = selected == ImportStrategy.REPLACE,
            onClick = { onSelect(ImportStrategy.REPLACE) },
        )
    }
}

@Composable
private fun RadioRow(
    label: String,
    checked: Boolean,
    onClick: () -> Unit,
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = checked, onClick = onClick, role = Role.RadioButton)
            .padding(vertical = 4.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
    ) {
        RadioButton(selected = checked, onClick = null)
        Text(
            text = label,
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
