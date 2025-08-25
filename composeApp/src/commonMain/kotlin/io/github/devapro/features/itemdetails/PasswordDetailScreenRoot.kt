package io.github.devapro.features.itemdetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.devapro.droid.core.ui.SnackbarHostStateManager
import io.github.devapro.droid.data.model.ItemModel
import io.github.devapro.droid.edit.navigation.AddEditPasswordScreen
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenAction
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenEvent
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenState
import io.github.devapro.features.itemdetails.ui.PasswordDetailScreenContent
import org.koin.compose.koinInject

@Composable
fun PasswordDetailScreenRoot(
    item: ItemModel
) {
    val viewModel: PasswordDetailScreenViewModel = koinInject()
    val snackbarManager: SnackbarHostStateManager = koinInject()

    val navigator = LocalNavigator.currentOrThrow
    val clipboard = LocalClipboardManager.current
    
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onAction(PasswordDetailScreenAction.InitScreen(item))
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect {
            when (it) {
                is PasswordDetailScreenEvent.NavigateBack -> {
                    navigator.pop()
                }
                is PasswordDetailScreenEvent.NavigateToEdit -> {
                    navigator.push(AddEditPasswordScreen(it.item))
                }
                is PasswordDetailScreenEvent.CopyToClipboard -> {
                    clipboard.setText(AnnotatedString(it.value))
                    snackbarManager.show("Copied to clipboard")
                }
                is PasswordDetailScreenEvent.ShareItem -> {
                    // Handle item sharing
                }
                is PasswordDetailScreenEvent.DeleteItem -> {
                    // Handle item deletion and navigate back
                    navigator.pop()
                }
                is PasswordDetailScreenEvent.ShowMessage -> {
                    // Show snackbar or toast
                }
            }
        }
    }

    when (state) {
        is PasswordDetailScreenState.Loading -> {
            // Show loading indicator if needed
        }
        is PasswordDetailScreenState.Error -> {
            // Show error state if needed
        }
        is PasswordDetailScreenState.Success -> {
            PasswordDetailScreenContent(
                state = state as PasswordDetailScreenState.Success,
                onAction = viewModel::onAction
            )
        }
    }
} 