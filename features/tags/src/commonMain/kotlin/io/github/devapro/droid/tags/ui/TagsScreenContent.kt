package io.github.devapro.droid.tags.ui

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import io.github.devapro.droid.core.ui.EOutlinedTextField
import io.github.devapro.droid.tags.model.TagsScreenAction
import io.github.devapro.droid.tags.model.TagsScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagsScreenContent(
    state: TagsScreenState.Success,
    onAction: (TagsScreenAction) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var isSearchActive by remember { mutableStateOf(state.hasSearchQuery) }
    val focusRequester = remember { FocusRequester() }
    val searchFieldFocusRequester = remember { FocusRequester() }

    // Update search active state when search query changes
    LaunchedEffect(state.hasSearchQuery) {
        if (!state.hasSearchQuery && isSearchActive) {
            isSearchActive = false
        }
    }

    // Request focus on initial load to enable hotkey detection
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    // Focus search field when search mode is activated
    LaunchedEffect(isSearchActive) {
        if (isSearchActive) {
            searchFieldFocusRequester.requestFocus()
        }
    }

    Scaffold(
        modifier = Modifier
            .focusRequester(focusRequester)
            .focusable()
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown) {
                    when {
                        // Cmd+F / Ctrl+F to activate search mode
                        keyEvent.key == Key.F &&
                        (keyEvent.isCtrlPressed || keyEvent.isMetaPressed) &&
                        !isSearchActive -> {
                            isSearchActive = true
                            true
                        }
                        // Esc to cancel search mode
                        keyEvent.key == Key.Escape && isSearchActive -> {
                            isSearchActive = false
                            onAction(TagsScreenAction.OnClearSearch)
                            focusRequester.requestFocus()
                            true
                        }
                        else -> false
                    }
                } else {
                    false
                }
            },
        topBar = {
            if (isSearchActive) {
                // Search bar as top bar
                TopAppBar(
                    title = {
                        EOutlinedTextField(
                            value = state.searchQuery,
                            onValueChange = { onAction(TagsScreenAction.OnSearchChanged(it)) },
                            placeholder = { Text("Search tags...") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(searchFieldFocusRequester)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            isSearchActive = false
                            onAction(TagsScreenAction.OnClearSearch)
                        }) {
                            Icon(Icons.Default.Clear, contentDescription = "Close search")
                        }
                    }
                )
            } else {
                // Normal top bar
                TopAppBar(
                    title = { Text("Tags") },
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
                                    onAction(TagsScreenAction.OnExportClicked)
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.ImportExport, contentDescription = null)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Settings") },
                                onClick = {
                                    showMenu = false
                                    onAction(TagsScreenAction.OnSettingsClicked)
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
                onClick = { onAction(TagsScreenAction.OnAddPasswordClicked) }
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
            // Tags list
            if (state.filteredTags.isEmpty()) {
                EmptyTagsState(
                    hasSearchQuery = state.hasSearchQuery
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = state.filteredTags,
                        key = { it.id }
                    ) { tag ->
                        TagItem(
                            tag = tag,
                            onItemClick = { onAction(TagsScreenAction.OnTagClicked(tag)) }
                        )
                    }
                }
            }
        }
    }
}