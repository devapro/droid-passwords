package io.github.devapro.ui.welcome.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.devapro.ui.welcome.model.WelcomeScreenAction
import io.github.devapro.ui.welcome.model.WelcomeScreenState

@Composable
fun WelcomeScreenContent(
    state: WelcomeScreenState,
    onAction: (WelcomeScreenAction) -> Unit
) {
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
                // App Icon
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "DroidPasswords",
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                
                // Welcome Title
                Text(
                    text = "Welcome to DroidPasswords",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                
                // Subtitle
                Text(
                    text = "Your secure password manager for all platforms",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Setup Options
                Text(
                    text = "How would you like to get started?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CreateNewVault(
                        onClick = {
                            onAction(WelcomeScreenAction.OnCreateNewVault)
                        }
                    )

                    ImportExistingVault(
                        onClick = {
                            onAction(WelcomeScreenAction.OnOpenExistingVault)
                        }
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Info Text
                Text(
                    text = "You can always import more passwords later from the main screen",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
} 