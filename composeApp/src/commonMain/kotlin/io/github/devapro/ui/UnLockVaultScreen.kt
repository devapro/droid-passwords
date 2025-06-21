package io.github.devapro.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun UnLockVaultScreen(
    onUnlock: (String) -> Unit,
    onSetupPassword: () -> Unit,
    hasLockPassword: Boolean,
    errorMessage: String? = null
) {
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Lock Icon
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Lock",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                
                // Title
                Text(
                    text = if (hasLockPassword) "Enter Password" else "Setup App Lock",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                
                // Subtitle
                Text(
                    text = if (hasLockPassword) 
                        "Enter your password to access your passwords" 
                    else 
                        "Set up a password to protect your password vault",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                
                if (hasLockPassword) {
                    // Password Input
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
                                )
                            }
                        },
                        isError = errorMessage != null
                    )
                    
                    // Error Message
                    if (errorMessage != null) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    }
                    
                    // Unlock Button
                    Button(
                        onClick = { onUnlock(password) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = password.isNotBlank(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        Text(
                            text = "Unlock",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                } else {
                    // Setup Password Button
                    Button(
                        onClick = onSetupPassword,
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        Text(
                            text = "Setup Password",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
                
                // App Info
                Text(
                    text = "DroidPasswords",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
} 