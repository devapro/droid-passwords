package io.github.devapro.droid.edit.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.github.devapro.droid.core.ui.EOutlinedTextField
import io.github.devapro.droid.edit.model.AddEditPasswordScreenAction
import io.github.devapro.droid.edit.model.AddEditPasswordScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditPasswordScreenContent(
    state: AddEditPasswordScreenState.Success,
    onAction: (AddEditPasswordScreenAction) -> Unit
) {
    Scaffold(
        topBar = {
            AddEditPasswordTopAppBar(
                state = state,
                onAction = onAction
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PasswordCoreFields(
                state = state,
                onAction = onAction
            )

            CollapsibleSection(
                title = "URL",
                expanded = state.isUrlVisible,
                onToggle = { onAction(AddEditPasswordScreenAction.OnToggleUrlSection) }
            ) {
                EOutlinedTextField(
                    value = state.url,
                    onValueChange = { onAction(AddEditPasswordScreenAction.OnUrlChanged(it)) },
                    label = { Text("URL") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
                )
            }

            CollapsibleSection(
                title = "Description",
                expanded = state.isDescriptionVisible,
                onToggle = { onAction(AddEditPasswordScreenAction.OnToggleDescriptionSection) }
            ) {
                EOutlinedTextField(
                    value = state.description,
                    onValueChange = { onAction(AddEditPasswordScreenAction.OnDescriptionChanged(it)) },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )
            }

            CollapsibleSection(
                title = "Two-factor (TOTP)",
                expanded = state.isTotpVisible,
                onToggle = { onAction(AddEditPasswordScreenAction.OnToggleTotpSection) }
            ) {
                TotpSecretField(
                    value = state.totpSecret,
                    onValueChange = { onAction(AddEditPasswordScreenAction.OnTotpSecretChanged(it)) }
                )
            }

            TagsSection(
                state = state,
                onAction = onAction
            )

            AdditionalFieldsSection(
                state = state,
                onAction = onAction
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "* Required fields",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }

    if (state.showDeleteConfirmation) {
        DeleteConfirmationDialog(
            onAction = onAction
        )
    }

    if (state.showGeneratorDialog) {
        PasswordGeneratorDialog(
            state = state,
            onAction = onAction
        )
    }
}

@Composable
private fun CollapsibleSection(
    title: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() }
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand"
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(bottom = 12.dp)) {
                    content()
                }
            }
        }
    }
}
