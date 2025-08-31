package io.github.devapro.droid.settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import io.github.devapro.droid.core.ui.EOutlinedTextField

/**
 * Dialog for changing the master password.
 *
 * @param isVisible Whether the dialog is visible
 * @param isLoading Whether the password change operation is in progress
 * @param errorMessage Error message to display, null if no error
 * @param onDismiss Callback when the dialog is dismissed
 * @param onPasswordChangeSubmitted Callback when password change is submitted
 */
@Composable
fun ChangePasswordDialog(
    isVisible: Boolean,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onDismiss: () -> Unit,
    onPasswordChangeSubmitted: (currentPassword: String, newPassword: String, confirmPassword: String) -> Unit
) {
    if (!isVisible) return

    // Form state
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Password visibility state
    var currentPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Focus management
    val focusManager = LocalFocusManager.current
    val currentPasswordFocusRequester = remember { FocusRequester() }
    val newPasswordFocusRequester = remember { FocusRequester() }
    val confirmPasswordFocusRequester = remember { FocusRequester() }

    // Form validation
    val isFormValid = currentPassword.isNotEmpty() &&
            newPassword.isNotEmpty() &&
            confirmPassword.isNotEmpty()

    // Client-side validation errors
    val currentPasswordError = if (currentPassword.isEmpty() && currentPassword != "") {
        "Current password cannot be empty"
    } else null

    val newPasswordError = when {
        newPassword.isEmpty() && newPassword != "" -> "New password cannot be empty"
        newPassword.isNotEmpty() && newPassword.length < 4 -> "Password must be at least 4 characters long"
        newPassword.isNotEmpty() && currentPassword.isNotEmpty() && newPassword == currentPassword ->
            "New password must be different from current password"

        else -> null
    }

    val confirmPasswordError = when {
        confirmPassword.isEmpty() && confirmPassword != "" -> "Password confirmation cannot be empty"
        confirmPassword.isNotEmpty() && newPassword.isNotEmpty() && confirmPassword != newPassword ->
            "Passwords do not match"

        else -> null
    }

    // Reset form when dialog becomes visible
    LaunchedEffect(isVisible) {
        if (isVisible) {
            currentPassword = ""
            newPassword = ""
            confirmPassword = ""
            currentPasswordVisible = false
            newPasswordVisible = false
            confirmPasswordVisible = false
        }
    }

    // Focus on first field when dialog opens
    LaunchedEffect(isVisible) {
        if (isVisible) {
            currentPasswordFocusRequester.requestFocus()
        }
    }

    AlertDialog(
        onDismissRequest = {
            if (!isLoading) {
                onDismiss()
            }
        },
        title = {
            Text(
                text = "Change Password",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Current Password Field
                EOutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = { Text("Current Password") },
                    placeholder = { Text("Enter current password") },
                    visualTransformation = if (currentPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { currentPasswordVisible = !currentPasswordVisible }
                        ) {
                            Icon(
                                imageVector = if (currentPasswordVisible) {
                                    Icons.Filled.VisibilityOff
                                } else {
                                    Icons.Filled.Visibility
                                },
                                contentDescription = if (currentPasswordVisible) {
                                    "Hide password"
                                } else {
                                    "Show password"
                                }
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    singleLine = true,
                    enabled = !isLoading,
                    isError = currentPasswordError != null,
                    supportingText = currentPasswordError?.let { { Text(it) } },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(currentPasswordFocusRequester)
                )

                // New Password Field
                EOutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Password") },
                    placeholder = { Text("Enter new password") },
                    visualTransformation = if (newPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { newPasswordVisible = !newPasswordVisible }
                        ) {
                            Icon(
                                imageVector = if (newPasswordVisible) {
                                    Icons.Filled.VisibilityOff
                                } else {
                                    Icons.Filled.Visibility
                                },
                                contentDescription = if (newPasswordVisible) {
                                    "Hide password"
                                } else {
                                    "Show password"
                                }
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    singleLine = true,
                    enabled = !isLoading,
                    isError = newPasswordError != null,
                    supportingText = newPasswordError?.let { { Text(it) } },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(newPasswordFocusRequester)
                )

                // Confirm Password Field
                EOutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm New Password") },
                    placeholder = { Text("Confirm new password") },
                    visualTransformation = if (confirmPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { confirmPasswordVisible = !confirmPasswordVisible }
                        ) {
                            Icon(
                                imageVector = if (confirmPasswordVisible) {
                                    Icons.Filled.VisibilityOff
                                } else {
                                    Icons.Filled.Visibility
                                },
                                contentDescription = if (confirmPasswordVisible) {
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
                            if (isFormValid && currentPasswordError == null &&
                                newPasswordError == null && confirmPasswordError == null && !isLoading
                            ) {
                                onPasswordChangeSubmitted(
                                    currentPassword,
                                    newPassword,
                                    confirmPassword
                                )
                            }
                        }
                    ),
                    singleLine = true,
                    enabled = !isLoading,
                    isError = confirmPasswordError != null,
                    supportingText = confirmPasswordError?.let { { Text(it) } },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(confirmPasswordFocusRequester)
                )

                // Server-side error message
                errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
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
                        if (!isLoading) {
                            onPasswordChangeSubmitted(currentPassword, newPassword, confirmPassword)
                        }
                    },
                    enabled = isFormValid &&
                            currentPasswordError == null &&
                            newPasswordError == null &&
                            confirmPasswordError == null &&
                            !isLoading
                ) {
                    Text(if (isLoading) "Changing..." else "Change Password")
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