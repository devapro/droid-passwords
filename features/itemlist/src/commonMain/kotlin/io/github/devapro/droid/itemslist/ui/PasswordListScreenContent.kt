package io.github.devapro.droid.itemslist.ui

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import io.github.devapro.droid.itemslist.model.PasswordListScreenAction
import io.github.devapro.droid.itemslist.model.PasswordListScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordListScreenContent(
    state: PasswordListScreenState.Success,
    onAction: (PasswordListScreenAction) -> Unit
) {
    var isSearchActive by remember { mutableStateOf(state.hasSearchQuery) }
    val focusRequester = remember { FocusRequester() }

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
                            onAction(PasswordListScreenAction.OnClearSearch)
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
                SearchTopAppBar(
                    state = state,
                    onAction = onAction,
                    onCancelButtonClicked = {
                        isSearchActive = false
                        onAction(PasswordListScreenAction.OnClearSearch)
                    }
                )
            } else {
                ScreenTopAppBar(
                    state = state,
                    onAction = onAction,
                    onSearchButtonClick = { isSearchActive = true }
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
        Box(
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
                ItemsList(
                    state = state,
                    onAction = onAction
                )
            }
        }
    }
}