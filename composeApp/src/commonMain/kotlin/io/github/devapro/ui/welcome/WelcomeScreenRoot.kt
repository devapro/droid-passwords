package io.github.devapro.ui.welcome

import androidx.compose.runtime.*
import io.github.devapro.ui.welcome.ui.WelcomeScreenContent
import org.koin.compose.koinInject

@Composable
fun WelcomeScreenRoot() {
    val viewModel: WelcomeScreenViewModel = koinInject()
    val state by viewModel.state.collectAsState()
    WelcomeScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
} 