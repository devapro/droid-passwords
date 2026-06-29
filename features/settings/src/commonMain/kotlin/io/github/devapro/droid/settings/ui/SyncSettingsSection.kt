package io.github.devapro.droid.settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenState

private val INTERVAL_OPTIONS = listOf(5, 15, 30, 60)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SyncSettingsSection(
    state: SettingsScreenState.Success,
    onAction: (SettingsScreenAction) -> Unit
) {
    SettingSectionHeader(title = "Sync")

    SettingClickableItem(
        title = "Sync Server",
        subtitle = state.syncServerUrl.ifEmpty { "Not configured (e.g. https://192.168.1.50:8443)" },
        leadingIcon = Icons.Default.Dns,
        onClick = { onAction(SettingsScreenAction.OnServerUrlClicked) }
    )

    if (!state.isLoggedIn) {
        SettingClickableItem(
            title = "Sign in / Register",
            subtitle = "Connect this device to your sync server",
            leadingIcon = Icons.Default.Person,
            onClick = { onAction(SettingsScreenAction.OnLoginClicked) }
        )
    } else {
        SettingClickableItem(
            title = "Account",
            subtitle = "Signed in as ${state.syncUsername}",
            leadingIcon = Icons.Default.Person,
            showTrailingArrow = false,
            onClick = {}
        )

        // Manual sync controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { onAction(SettingsScreenAction.OnSyncToServerClicked) },
                enabled = !state.isSyncing,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.CloudUpload, contentDescription = null)
                Text("  To server")
            }
            OutlinedButton(
                onClick = { onAction(SettingsScreenAction.OnSyncFromServerClicked) },
                enabled = !state.isSyncing,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.CloudDownload, contentDescription = null)
                Text("  From server")
            }
        }

        Button(
            onClick = { onAction(SettingsScreenAction.OnSyncNowClicked) },
            enabled = !state.isSyncing,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Icon(Icons.Default.Sync, contentDescription = null)
            Text("  Sync now")
        }

        // Periodic sync toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Periodic sync",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Automatically sync in the background while the app is open",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                checked = state.periodicSyncEnabled,
                onCheckedChange = { onAction(SettingsScreenAction.OnPeriodicSyncToggled(it)) }
            )
        }

        if (state.periodicSyncEnabled) {
            Text(
                text = "Sync interval",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                INTERVAL_OPTIONS.forEach { minutes ->
                    FilterChip(
                        selected = state.periodicSyncIntervalMinutes == minutes,
                        onClick = { onAction(SettingsScreenAction.OnPeriodicIntervalChanged(minutes)) },
                        label = { Text(if (minutes < 60) "$minutes min" else "1 hour") }
                    )
                }
            }
        }

        if (state.lastSyncStatus.isNotEmpty()) {
            Text(
                text = "Last sync: ${state.lastSyncStatus}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        SettingClickableItem(
            title = "Sign out",
            subtitle = "Disconnect this device from the sync server",
            leadingIcon = Icons.AutoMirrored.Filled.Logout,
            showTrailingArrow = false,
            onClick = { onAction(SettingsScreenAction.OnLogoutClicked) }
        )
    }
}
