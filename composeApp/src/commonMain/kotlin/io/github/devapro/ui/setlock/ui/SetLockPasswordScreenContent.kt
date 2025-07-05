package io.github.devapro.ui.setlock.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import io.github.devapro.core.ui.EOutlinedTextField
import io.github.devapro.ui.setlock.model.SetLockPasswordScreenAction
import io.github.devapro.ui.setlock.model.SetLockPasswordScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetLockPasswordScreenContent(
    state: SetLockPasswordScreenState.Success,
    onAction: (SetLockPasswordScreenAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (state.isVaultExists) "Change Lock Password" else "Set Lock Password"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(SetLockPasswordScreenAction.OnBackClicked) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = if (state.isVaultExists) {
                            "Change your lock password"
                        } else {
                            "Set a password to secure your vault"
                        },
                        style = MaterialTheme.typography.bodyLarge
                    )

                    if (state.isVaultExists) {
                        EOutlinedTextField(
                            value = state.currentPassword,
                            onValueChange = { onAction(SetLockPasswordScreenAction.OnCurrentPasswordChanged(it)) },
                            label = { Text("Current Password") },
                            visualTransformation = if (state.isCurrentPasswordVisible) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = { onAction(SetLockPasswordScreenAction.OnToggleCurrentPasswordVisibility) }
                                ) {
                                    Icon(
                                        imageVector = if (state.isCurrentPasswordVisible) {
                                            Icons.Default.VisibilityOff
                                        } else {
                                            Icons.Default.Visibility
                                        },
                                        contentDescription = if (state.isCurrentPasswordVisible) {
                                            "Hide password"
                                        } else {
                                            "Show password"
                                        }
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            isError = state.currentPasswordError != null,
                            supportingText = state.currentPasswordError?.let { { Text(it) } },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    EOutlinedTextField(
                        value = state.newPassword,
                        onValueChange = { onAction(SetLockPasswordScreenAction.OnNewPasswordChanged(it)) },
                        label = { Text("New Password") },
                        visualTransformation = if (state.isNewPasswordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { onAction(SetLockPasswordScreenAction.OnToggleNewPasswordVisibility) }
                            ) {
                                Icon(
                                    imageVector = if (state.isNewPasswordVisible) {
                                        Icons.Default.VisibilityOff
                                    } else {
                                        Icons.Default.Visibility
                                    },
                                    contentDescription = if (state.isNewPasswordVisible) {
                                        "Hide password"
                                    } else {
                                        "Show password"
                                    }
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        isError = state.newPasswordError != null,
                        supportingText = state.newPasswordError?.let { { Text(it) } },
                        modifier = Modifier.fillMaxWidth()
                    )

                    EOutlinedTextField(
                        value = state.confirmPassword,
                        onValueChange = { onAction(SetLockPasswordScreenAction.OnConfirmPasswordChanged(it)) },
                        label = { Text("Confirm Password") },
                        visualTransformation = if (state.isConfirmPasswordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { onAction(SetLockPasswordScreenAction.OnToggleConfirmPasswordVisibility) }
                            ) {
                                Icon(
                                    imageVector = if (state.isConfirmPasswordVisible) {
                                        Icons.Default.VisibilityOff
                                    } else {
                                        Icons.Default.Visibility
                                    },
                                    contentDescription = if (state.isConfirmPasswordVisible) {
                                        "Hide password"
                                    } else {
                                        "Show password"
                                    }
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        isError = state.confirmPasswordError != null,
                        supportingText = state.confirmPasswordError?.let { { Text(it) } },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Action buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onAction(SetLockPasswordScreenAction.OnSaveClicked) },
                    enabled = state.isValid && !state.isProcessing,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (state.isProcessing) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.padding(4.dp)
                            )
                            Text("Saving...")
                        }
                    } else {
                        Text(if (state.isVaultExists) "Change Password" else "Set Password")
                    }
                }

                if (state.isVaultExists) {
                    OutlinedButton(
                        onClick = { onAction(SetLockPasswordScreenAction.OnRemovePasswordClicked) },
                        enabled = !state.isProcessing,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Remove Password")
                    }
                }
            }
        }
    }
} 