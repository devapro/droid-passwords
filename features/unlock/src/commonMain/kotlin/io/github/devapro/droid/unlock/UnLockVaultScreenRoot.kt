package io.github.devapro.droid.unlock

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.devapro.droid.core.ui.SnackbarHostStateManager
import io.github.devapro.droid.tags.navigation.TagsScreen
import io.github.devapro.droid.unlock.model.UnLockVaultScreenAction
import io.github.devapro.droid.unlock.model.UnLockVaultScreenEvent
import io.github.devapro.droid.unlock.model.UnLockVaultScreenState
import io.github.devapro.droid.unlock.ui.UnLockVaultScreenContent
import org.koin.compose.koinInject

@Composable
fun UnLockVaultScreenRoot() {
    val viewModel: UnLockVaultScreenViewModel = koinInject()
    val snackBarManager: SnackbarHostStateManager = koinInject()
    val navigator = LocalNavigator.currentOrThrow
    
    val state by viewModel.state.collectAsState()
    val event by viewModel.event.collectAsState(initial = null)

    LaunchedEffect(Unit) {
        viewModel.onAction(UnLockVaultScreenAction.InitScreen)
    }

    LaunchedEffect(event) {
        viewModel.event.collect {
            when (it) {
                is UnLockVaultScreenEvent.NavigateBack -> {
                    navigator.pop()
                }
                is UnLockVaultScreenEvent.UnlockSuccess -> {
                    navigator.pop()
                    navigator.push(TagsScreen)
                }

                is UnLockVaultScreenEvent.ShowError -> {
                    snackBarManager.show(
                        message = it.error,
                        actionButtonText = "OK",
                        duration = SnackbarDuration.Short,
                        actionButtonCallback = { }
                    )
                }
            }
        }
    }

    when (state) {
        is UnLockVaultScreenState.Loading -> {
            // Show loading indicator if needed
        }
        is UnLockVaultScreenState.Loaded -> {
            UnLockVaultScreenContent(
                state = state as UnLockVaultScreenState.Loaded,
                onAction = viewModel::onAction
            )
        }
    }
} 