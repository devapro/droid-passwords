package io.github.devapro.features.itemslist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.devapro.core.ui.EOutlinedTextField
import io.github.devapro.features.itemslist.model.PasswordListScreenAction
import io.github.devapro.features.itemslist.model.PasswordListScreenState
import io.github.devapro.model.ItemModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordListScreenContent(
    state: PasswordListScreenState.Success,
    onAction: (PasswordListScreenAction) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var isSearchActive by remember { mutableStateOf(state.hasSearchQuery) }

    // Update search active state when search query changes
    LaunchedEffect(state.hasSearchQuery) {
        if (!state.hasSearchQuery && isSearchActive) {
            isSearchActive = false
        }
    }

    Scaffold(
        topBar = {
            if (isSearchActive) {
                // Search bar as top bar
                TopAppBar(
                    title = {
                        EOutlinedTextField(
                            value = state.searchQuery,
                            onValueChange = { onAction(PasswordListScreenAction.OnSearchChanged(it)) },
                            placeholder = { Text("Search passwords...") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            isSearchActive = false
                            onAction(PasswordListScreenAction.OnClearSearch)
                        }) {
                            Icon(Icons.Default.Clear, contentDescription = "Close search")
                        }
                    }
                )
            } else {
                // Normal top bar
                TopAppBar(
                    title = {
                        Text(
                            text = state.title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            onAction(PasswordListScreenAction.OnBackClicked)
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { isSearchActive = true }) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More options")
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Export") },
                                onClick = {
                                    showMenu = false
                                    onAction(PasswordListScreenAction.OnExportClicked)
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.ImportExport, contentDescription = null)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Settings") },
                                onClick = {
                                    showMenu = false
                                    onAction(PasswordListScreenAction.OnSettingsClicked)
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Settings, contentDescription = null)
                                }
                            )
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(PasswordListScreenAction.OnAddPasswordClicked) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add password")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Password list
            if (state.filteredPasswords.isEmpty()) {
                EmptyState(
                    hasSearchQuery = state.hasSearchQuery
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = state.filteredPasswords,
                        key = { it.id }
                    ) { password ->
                        PasswordItem(
                            password = password,
                            onItemClick = {
                                onAction(
                                    PasswordListScreenAction.OnPasswordItemClicked(
                                        password
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PasswordItem(
    password: ItemModel,
    onItemClick: () -> Unit
) {
    Card(
        onClick = onItemClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = password.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (password.username.isNotEmpty()) {
                Text(
                    text = password.username,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            if (password.url.isNotEmpty()) {
                Text(
                    text = password.url,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
} 