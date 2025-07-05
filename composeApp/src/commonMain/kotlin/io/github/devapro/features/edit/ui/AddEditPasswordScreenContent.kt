package io.github.devapro.features.edit.ui

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GeneratingTokens
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.devapro.core.ui.EOutlinedTextField
import io.github.devapro.features.edit.model.AddEditPasswordScreenAction
import io.github.devapro.features.edit.model.AddEditPasswordScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditPasswordScreenContent(
    state: AddEditPasswordScreenState.Success,
    onAction: (AddEditPasswordScreenAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (state.isEditMode) "Edit Password" else "Add Password",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(AddEditPasswordScreenAction.OnBackClicked) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (state.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    } else {
                        IconButton(
                            onClick = { onAction(AddEditPasswordScreenAction.OnSaveClicked) },
                            enabled = state.isFormValid
                        ) {
                            Icon(
                                Icons.Default.Save,
                                contentDescription = "Save",
                                tint = if (state.isFormValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.38f
                                )
                            )
                        }
                    }
                    if (state.isEditMode) {
                        IconButton(onClick = { onAction(AddEditPasswordScreenAction.OnDeleteClicked) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title Field
            EOutlinedTextField(
                value = state.title,
                onValueChange = { onAction(AddEditPasswordScreenAction.OnTitleChanged(it)) },
                label = { Text("Title *") },
                modifier = Modifier.fillMaxWidth(),
                isError = state.titleError != null,
                supportingText = state.titleError?.let { { Text(it) } }
            )

            // Username Field
            EOutlinedTextField(
                value = state.username,
                onValueChange = { onAction(AddEditPasswordScreenAction.OnUsernameChanged(it)) },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            // Password Field
            EOutlinedTextField(
                value = state.password,
                onValueChange = { onAction(AddEditPasswordScreenAction.OnPasswordChanged(it)) },
                label = { Text("Password *") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    Row {
                        IconButton(onClick = { onAction(AddEditPasswordScreenAction.OnGeneratePassword) }) {
                            Icon(
                                Icons.Default.GeneratingTokens,
                                contentDescription = "Generate password",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(onClick = { onAction(AddEditPasswordScreenAction.OnTogglePasswordVisibility) }) {
                            Icon(
                                imageVector = if (state.isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (state.isPasswordVisible) "Hide password" else "Show password"
                            )
                        }
                    }
                },
                isError = state.passwordError != null,
                supportingText = state.passwordError?.let { { Text(it) } }
            )

            // Collapsible Additional Fields Section
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
                                onValueChange = {
                                    onAction(
                                        AddEditPasswordScreenAction.OnUrlChanged(
                                            it
                                        )
                                    )
                                },
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


            Spacer(modifier = Modifier.height(16.dp))

            // Required fields note
            Text(
                text = "* Required fields",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
} 