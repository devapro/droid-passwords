package io.github.devapro.droid.setlock

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.devapro.droid.setlock.model.SetLockPasswordScreenAction
import io.github.devapro.droid.setlock.model.SetLockPasswordScreenEvent
import io.github.devapro.droid.setlock.model.SetLockPasswordScreenState
import io.github.devapro.droid.setlock.ui.SetLockPasswordScreenContent
import io.github.devapro.droid.tags.navigation.TagsScreen
import org.koin.compose.koinInject

@Composable
fun SetLockPasswordScreenRoot() {
    val viewModel: SetLockPasswordScreenViewModel = koinInject()
    val navigator = LocalNavigator.currentOrThrow

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onAction(SetLockPasswordScreenAction.InitScreen)
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect {
            when (it) {
                is SetLockPasswordScreenEvent.NavigateBack -> {
                    navigator.pop()
                }
                is SetLockPasswordScreenEvent.ShowSuccess -> {
                    navigator.pop()
                    navigator.push(TagsScreen)
                }
                else -> { /* Handle other events if needed */ }
            }
        }
    }

    when (state) {
        is SetLockPasswordScreenState.Loading -> {
            // Show loading indicator if needed
        }
        is SetLockPasswordScreenState.Error -> {
            // Show error state if needed
        }
        is SetLockPasswordScreenState.Success -> {
            SetLockPasswordScreenContent(
                state = state as SetLockPasswordScreenState.Success,
                onAction = viewModel::onAction
            )
        }
    }
} 