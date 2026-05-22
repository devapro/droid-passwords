package io.github.devapro.droid.importdata.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.devapro.droid.importdata.model.ImportTarget

@Composable
fun ImportTargetCard(
    selected: ImportTarget,
    activeVaultName: String,
    onSelected: (ImportTarget) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
        ) {
            Text(
                text = "What do you want to do?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Column(modifier = Modifier.selectableGroup().padding(top = 4.dp)) {
                TargetRow(
                    label = "Add as new vault",
                    description = "Imported items go into a new vault file you can switch to.",
                    checked = selected == ImportTarget.NEW_VAULT,
                    onClick = { onSelected(ImportTarget.NEW_VAULT) },
                )
                TargetRow(
                    label = if (activeVaultName.isNotBlank()) "Merge into \"$activeVaultName\"" else "Merge into active vault",
                    description = "Imported items are folded into the currently active vault. You'll pick a strategy.",
                    checked = selected == ImportTarget.MERGE_INTO_ACTIVE,
                    onClick = { onSelected(ImportTarget.MERGE_INTO_ACTIVE) },
                )
            }
        }
    }
}

@Composable
private fun TargetRow(
    label: String,
    description: String,
    checked: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = checked, onClick = onClick, role = Role.RadioButton)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top,
    ) {
        RadioButton(selected = checked, onClick = null)
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
