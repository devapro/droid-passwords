package io.github.devapro.droid.importdata.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import io.github.devapro.droid.core.ui.EOutlinedTextField
import io.github.devapro.droid.importdata.model.ImportScreenAction
import io.github.devapro.droid.importdata.model.ImportScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportLoadedUi(
    state: ImportScreenState.Loaded,
    onAction: (ImportScreenAction) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Import Passwords") },
                navigationIcon = {
                    IconButton(onClick = { onAction(ImportScreenAction.OnBackClicked) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                EOutlinedTextField(
                    value = state.password,
                    onValueChange = { onAction(ImportScreenAction.OnPasswordChanged(it)) },
                    label = { Text("Password") },
                    placeholder = { Text("Enter your password") },
                    visualTransformation = if (state.isPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { onAction(ImportScreenAction.OnTogglePasswordVisibility) }
                        ) {
                            Icon(
                                imageVector = if (state.isPasswordVisible) {
                                    Icons.Default.VisibilityOff
                                } else {
                                    Icons.Default.Visibility
                                },
                                contentDescription = if (state.isPasswordVisible) {
                                    "Hide password"
                                } else {
                                    "Show password"
                                }
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            if (state.isValid) {
                                onAction(ImportScreenAction.OnUnlockClicked)
                            }
                        }
                    ),
                    isError = state.passwordError != null,
                    supportingText = state.passwordError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
            // File Format Selection
            FormatSelectionCard(
                state = state,
                onFormatSelected = { format ->
                    onAction(ImportScreenAction.OnFormatSelected(format))
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Action Button
            Button(
                onClick = {
                    onAction(ImportScreenAction.OnImportClicked)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isProcessing && state.isValid,
                contentPadding = PaddingValues(16.dp)
            ) {
                if (state.isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Upload,
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Import from ${state.selectedFormat.name}",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Warning/Info Text
            Text(
                text = "⚠️ Import will add new passwords to your existing collection. Duplicates may be created.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}