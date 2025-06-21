package io.github.devapro.ui.itemslist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.devapro.ui.itemslist.model.PasswordListScreenAction
import io.github.devapro.ui.itemslist.model.PasswordListScreenEvent
import io.github.devapro.ui.itemslist.model.PasswordListScreenState
import io.github.devapro.ui.itemslist.ui.PasswordListScreenContent
import org.koin.compose.koinInject

@Composable
fun PasswordListScreenRoot() {
    val viewModel: PasswordListScreenViewModel = koinInject()
    val navigator = LocalNavigator.currentOrThrow
    
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onAction(PasswordListScreenAction.InitScreen)
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect {
            when (it) {
                is PasswordListScreenEvent.NavigateToAddPassword -> {
                    // Navigate to add password screen
                    // navigator.push(AddEditPasswordScreen())
                }
                is PasswordListScreenEvent.NavigateToPasswordDetail -> {
                    // Navigate to password detail screen
                    // navigator.push(PasswordDetailScreen(it.item))
                }
                is PasswordListScreenEvent.NavigateToImportExport -> {
                    // Navigate to import/export screen
                    // navigator.push(ImportExportScreen())
                }
                is PasswordListScreenEvent.NavigateToSettings -> {
                    // Navigate to settings screen
                    // navigator.push(SettingsScreen())
                }
                is PasswordListScreenEvent.DeletePassword -> {
                    // Handle password deletion
                }
                is PasswordListScreenEvent.RefreshPasswordList -> {
                    // Handle password list refresh
                }
            }
        }
    }

    when (state) {
        is PasswordListScreenState.Loading -> {
            // Show loading indicator if needed
        }
        is PasswordListScreenState.Error -> {
            // Show error state if needed
        }
        is PasswordListScreenState.Success -> {
            PasswordListScreenContent(
                state = state as PasswordListScreenState.Success,
                onAction = viewModel::onAction
            )
        }
    }
} 