package io.github.devapro.droid.edit

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.devapro.droid.core.ui.SnackbarHostStateManager
import io.github.devapro.droid.data.model.ItemModel
import io.github.devapro.droid.edit.model.AddEditPasswordScreenAction
import io.github.devapro.droid.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.droid.edit.model.AddEditPasswordScreenState
import io.github.devapro.droid.edit.ui.AddEditPasswordScreenContent
import kotlinx.coroutines.delay
import org.koin.compose.koinInject

@Composable
fun AddEditPasswordScreenRoot(
    item: ItemModel? = null
) {
    val viewModel: AddEditPasswordScreenViewModel = koinInject()
    val snackBarManager: SnackbarHostStateManager = koinInject()
    val navigator = LocalNavigator.currentOrThrow
    
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onAction(AddEditPasswordScreenAction.InitScreen(item))
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect {
            when (it) {
                is AddEditPasswordScreenEvent.NavigateBack -> {
                    navigator.pop()
                }
                is AddEditPasswordScreenEvent.SaveSuccess -> {
                    snackBarManager.show(
                        message = "Password saved successfully",
                        actionButtonText = "OK",
                        duration = SnackbarDuration.Short,
                        actionButtonCallback = {
                            navigator.pop()
                        }
                    )
                    delay(1000L)
                    navigator.pop()
                }
                is AddEditPasswordScreenEvent.SaveError -> {
                    // Handle save error - show snackbar
                }
                is AddEditPasswordScreenEvent.DeleteSuccess -> {
                    snackBarManager.show(
                        message = "Password deleted successfully",
                        actionButtonText = "OK",
                        duration = SnackbarDuration.Short,
                        actionButtonCallback = { }
                    )
                    navigator.pop()
                }
                is AddEditPasswordScreenEvent.DeleteError -> {
                    snackBarManager.show(
                        message = it.message,
                        actionButtonText = "OK",
                        duration = SnackbarDuration.Short,
                        actionButtonCallback = { }
                    )
                    navigator.pop()
                }
                is AddEditPasswordScreenEvent.GeneratedPassword -> {
                    snackBarManager.show(
                        message = "Password generated",
                        actionButtonText = "Copy",
                        duration = SnackbarDuration.Short,
                        actionButtonCallback = {
                            // Handle copy to clipboard
                        }
                    )
                }
            }
        }
    }

    when (state) {
        is AddEditPasswordScreenState.Loading -> {
            // Show loading indicator if needed
        }
        is AddEditPasswordScreenState.Error -> {
            // Show error state if needed
        }
        is AddEditPasswordScreenState.Success -> {
            AddEditPasswordScreenContent(
                state = state as AddEditPasswordScreenState.Success,
                onAction = viewModel::onAction
            )
        }
    }
} 