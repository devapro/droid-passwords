package io.github.devapro.features.edit.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GeneratingTokens
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import io.github.devapro.droid.core.ui.EOutlinedTextField
import io.github.devapro.features.edit.model.AddEditPasswordScreenAction
import io.github.devapro.features.edit.model.AddEditPasswordScreenState

@Composable
fun PasswordCoreFields(
    state: AddEditPasswordScreenState.Success,
    onAction: (AddEditPasswordScreenAction) -> Unit
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
}