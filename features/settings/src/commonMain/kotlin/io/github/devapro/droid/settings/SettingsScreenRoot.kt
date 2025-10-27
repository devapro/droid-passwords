package io.github.devapro.droid.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.devapro.droid.core.ui.SnackbarHostStateManager
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState
import io.github.devapro.droid.settings.ui.SettingsScreenContent
import org.koin.compose.koinInject

/**
 * Root composable for the Settings Screen that integrates ViewModel, navigation, and user feedback.
 *
 * This composable serves as the main entry point for the settings screen and handles:
 * - ViewModel integration and state management
 * - Navigation events (back navigation)
 * - Dialog state management and display
 * - User feedback through SnackbarHostStateManager
 * - Screen initialization
 *
 * The Root composable follows the established pattern used throughout the application
 * and ensures proper integration with the MVI architecture and navigation system.
 *
 * Requirements covered:
 * - 5.1: Provides navigation integration for settings screen
 * - 5.2: Handles back navigation and screen transitions
 * - 5.3: Manages settings changes and user feedback
 * - 5.4: Integrates with ViewModel for state management
 * - 5.5: Provides user feedback through snackbar messages
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenRoot() {
    val viewModel: SettingsScreenViewModel = koinInject()
    val snackbarManager: SnackbarHostStateManager = koinInject()
    val navigator = LocalNavigator.currentOrThrow

    val state by viewModel.state.collectAsState()

    // Initialize the screen when the composable is first created
    LaunchedEffect(Unit) {
        viewModel.onAction(SettingsScreenAction.InitScreen)
    }

    // Handle navigation events and user feedback
    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is SettingsScreenEvent.NavigateBack -> {
                    navigator.pop()
                }

                is SettingsScreenEvent.ShowError -> {
                    snackbarManager.show(
                        message = event.message
                    )
                }

                is SettingsScreenEvent.ShowSuccess -> {
                    snackbarManager.show(
                        message = event.message
                    )
                }

                is SettingsScreenEvent.ShowFilePathPicker -> {
                    // File path picker is handled within the dialog
                    // This event could be used for platform-specific file picker integration
                }
            }
        }
    }

    // Main UI structure with Scaffold for snackbar integration
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navigator.pop()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {}
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (state) {
                is SettingsScreenState.Loading -> {
                    // Show loading indicator if needed
                    // For now, we'll just show empty content as loading is typically fast
                }

                is SettingsScreenState.Error -> {
                    // Error state is handled through events and snackbar
                    // We could add a dedicated error UI here if needed
                }

                is SettingsScreenState.Success -> {
                    SettingsScreenContent(
                        state = state as SettingsScreenState.Success,
                        onAction = viewModel::onAction
                    )
                }
            }
        }
    }
}