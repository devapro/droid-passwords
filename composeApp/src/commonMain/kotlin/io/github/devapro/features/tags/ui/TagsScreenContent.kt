package io.github.devapro.features.tags.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import io.github.devapro.core.ui.EOutlinedTextField
import io.github.devapro.features.tags.model.TagsScreenAction
import io.github.devapro.features.tags.model.TagsScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagsScreenContent(
    state: TagsScreenState.Success,
    onAction: (TagsScreenAction) -> Unit
) {
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
                            onValueChange = { onAction(TagsScreenAction.OnSearchChanged(it)) },
                            placeholder = { Text("Search tags...") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
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
                    }
                )
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