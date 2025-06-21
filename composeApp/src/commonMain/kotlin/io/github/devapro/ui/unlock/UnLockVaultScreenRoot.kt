package io.github.devapro.ui.unlock

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.devapro.ui.unlock.model.UnLockVaultScreenAction
import io.github.devapro.ui.unlock.model.UnLockVaultScreenEvent
import io.github.devapro.ui.unlock.model.UnLockVaultScreenState
import io.github.devapro.ui.unlock.ui.UnLockVaultScreenContent
import org.koin.compose.koinInject

@Composable
fun UnLockVaultScreenRoot() {
    val viewModel: UnLockVaultScreenViewModel = koinInject()
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
                }
                is UnLockVaultScreenEvent.UnlockVault -> {
                    // Handle unlock vault event
                }
            }
        }
    }

    when (state) {
        is UnLockVaultScreenState.Loading -> {
            // Show loading indicator if needed
        }
        is UnLockVaultScreenState.Error -> {
            // Show error state if needed
        }
        is UnLockVaultScreenState.Success -> {
            UnLockVaultScreenContent(
                state = state as UnLockVaultScreenState.Success,
                onAction = viewModel::onAction
            )
        }
    }
} 