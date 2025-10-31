package io.github.devapro.droid.welcome.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.devapro.droid.welcome.model.WelcomeScreenAction
import io.github.devapro.droid.welcome.model.WelcomeScreenState

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
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            ) {
                val isWideLayout = maxWidth >= 600.dp

                if (isWideLayout) {
                    // Two-column layout for landscape/large devices
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(48.dp)
                    ) {
                        // Left column: Icon and branding
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = "DroidPasswords",
                                modifier = Modifier.size(100.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = "Welcome to DroidPasswords",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Your secure password manager for all platforms",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = "You can always import more passwords later from the main screen",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // Right column: Actions
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "How would you like to get started?",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            WelcomeActions(state = state, onAction = onAction)
                        }
                    }
                } else {
                    // Single-column layout for portrait/small devices
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "DroidPasswords",
                            modifier = Modifier.size(80.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = "Welcome to DroidPasswords",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "Your secure password manager for all platforms",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "How would you like to get started?",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )

                        WelcomeActions(state = state, onAction = onAction)

                        Spacer(modifier = Modifier.height(8.dp))

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
    }
}

@Composable
private fun WelcomeActions(
    state: WelcomeScreenState,
    onAction: (WelcomeScreenAction) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        when (state) {
            is WelcomeScreenState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            is WelcomeScreenState.Error -> {
                Text(
                    text = "An error occurred. Please try again.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            is WelcomeScreenState.Success -> {
                if (state.isVaultExists) {
                    LoadVault(
                        onClick = {
                            onAction(WelcomeScreenAction.OnLoadVault)
                        }
                    )
                } else {
                    CreateNewVault(
                        onClick = {
                            onAction(WelcomeScreenAction.OnCreateNewVault)
                        }
                    )
                }
                ImportExistingVault(
                    onClick = {
                        onAction(WelcomeScreenAction.OnOpenExistingVault)
                    }
                )
            }
        }
    }
} 