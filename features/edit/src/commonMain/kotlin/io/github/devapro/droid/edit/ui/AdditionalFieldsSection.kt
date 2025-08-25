package io.github.devapro.droid.edit.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.github.devapro.droid.core.ui.EOutlinedTextField
import io.github.devapro.droid.edit.model.AddEditPasswordScreenAction
import io.github.devapro.droid.edit.model.AddEditPasswordScreenState

@Composable
fun AdditionalFieldsSection(
    state: AddEditPasswordScreenState.Success,
    onAction: (AddEditPasswordScreenAction) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAction(AddEditPasswordScreenAction.OnToggleAdditionalFields) }
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Additional Fields",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = if (state.isAdditionalFieldsVisible) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (state.isAdditionalFieldsVisible) "Collapse" else "Expand"
                )
            }

            AnimatedVisibility(visible = state.isAdditionalFieldsVisible) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // URL Field
                    EOutlinedTextField(
                        value = state.url,
                        onValueChange = { onAction(AddEditPasswordScreenAction.OnUrlChanged(it)) },
                        label = { Text("URL") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
                    )

                    // Description Field
                    EOutlinedTextField(
                        value = state.description,
                        onValueChange = {
                            onAction(
                                AddEditPasswordScreenAction.OnDescriptionChanged(
                                    it
                                )
                            )
                        },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5
                    )

                    // Additional Fields Section
                    Text(
                        text = "Custom Fields",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    state.additionalFields.forEachIndexed { index, field ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    EOutlinedTextField(
                                        value = field.name,
                                        onValueChange = {
                                            onAction(
                                                AddEditPasswordScreenAction.OnAdditionalFieldNameChanged(
                                                    index,
                                                    it
                                                )
                                            )
                                        },
                                        label = { Text("Field Name") },
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(onClick = {
                                        onAction(
                                            AddEditPasswordScreenAction.OnRemoveAdditionalField(
                                                index
                                            )
                                        )
                                    }) {
                                        Icon(
                                            Icons.Default.Remove,
                                            contentDescription = "Remove field",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                                EOutlinedTextField(
                                    value = field.value,
                                    onValueChange = {
                                        onAction(
                                            AddEditPasswordScreenAction.OnAdditionalFieldValueChanged(
                                                index,
                                                it
                                            )
                                        )
                                    },
                                    label = { Text("Field Value") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    TextButton(
                        onClick = { onAction(AddEditPasswordScreenAction.OnAddAdditionalField) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add field")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Custom Field")
                    }
                }
            }
        }
    }
}