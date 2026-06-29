package io.github.devapro.droid.vaultlist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.devapro.droid.vaultlist.model.VaultListItem
import io.github.devapro.droid.vaultlist.model.VaultListScreenAction
import io.github.devapro.droid.vaultlist.model.VaultListScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaultListScreenContent(
    state: VaultListScreenState,
    onAction: (VaultListScreenAction) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vaults") },
                navigationIcon = {
                    IconButton(onClick = { onAction(VaultListScreenAction.OnBackClicked) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (state is VaultListScreenState.Loaded && state.isLoggedIn) {
                        IconButton(onClick = { onAction(VaultListScreenAction.OnLogoutClicked) }) {
                            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Sign out")
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            if (state is VaultListScreenState.Loaded) {
                Box {
                    FloatingActionButton(onClick = { onAction(VaultListScreenAction.OnAddVaultClicked) }) {
                        Icon(Icons.Default.Add, contentDescription = "Add vault")
                    }
                    DropdownMenu(
                        expanded = state.showAddMenu,
                        onDismissRequest = { onAction(VaultListScreenAction.OnDismissAddMenu) },
                    ) {
                        DropdownMenuItem(
                            text = { Text("Create new vault") },
                            onClick = { onAction(VaultListScreenAction.OnCreateNewVaultSelected) },
                        )
                        DropdownMenuItem(
                            text = { Text("Import vault from file") },
                            onClick = { onAction(VaultListScreenAction.OnImportVaultSelected) },
                        )
                    }
                }
            }
        },
    ) { paddingValues ->
        when (state) {
            is VaultListScreenState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center,
                ) { CircularProgressIndicator() }
            }

            is VaultListScreenState.Loaded -> {
                PullToRefreshBox(
                    isRefreshing = state.isSyncing,
                    onRefresh = { onAction(VaultListScreenAction.OnRefresh) },
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                ) {
                    if (state.vaults.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(24.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            if (state.isSyncing) {
                                CircularProgressIndicator()
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Loading vaults…",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Medium,
                                )
                            } else {
                                Text(
                                    text = "No vaults yet",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.SemiBold,
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Tap + to create or import a vault.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            items(state.vaults, key = { it.descriptor.id }) { item ->
                                VaultListRow(
                                    item = item,
                                    isActive = item.descriptor.id == state.activeVaultId,
                                    onClick = { onAction(VaultListScreenAction.OnVaultClicked(item.descriptor)) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VaultListRow(
    item: VaultListItem,
    isActive: Boolean,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = if (item.isLoaded) Icons.Default.LockOpen else Icons.Default.Lock,
                contentDescription = if (item.isLoaded) "Unlocked" else "Locked",
                tint = if (item.isLoaded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp),
            )
            Spacer(modifier = Modifier.size(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.descriptor.name.ifBlank { "Unnamed vault" },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = if (item.isLoaded) "Unlocked this session" else "Tap to unlock",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (isActive) {
                AssistChip(
                    onClick = onClick,
                    label = { Text("Active") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(AssistChipDefaults.IconSize),
                        )
                    },
                )
            }
        }
    }
}
