package io.github.devapro.features.welcome

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.devapro.features.importexport.navigation.ImportExportScreen
import io.github.devapro.features.setlock.navigation.SetLockPasswordScreen
import io.github.devapro.features.unlock.navigation.UnLockVaultScreen
import io.github.devapro.features.welcome.model.WelcomeScreenAction
import io.github.devapro.features.welcome.model.WelcomeScreenEvent
import io.github.devapro.features.welcome.ui.WelcomeScreenContent
import org.koin.compose.koinInject

@Composable
fun WelcomeScreenRoot() {
    val viewModel: WelcomeScreenViewModel = koinInject()
    val state by viewModel.state.collectAsState()
    WelcomeScreenContent(
        state = state,
        onAction = viewModel::onAction
    )

    val navigator = LocalNavigator.currentOrThrow
    LaunchedEffect(Unit) {
        viewModel.onAction(WelcomeScreenAction.InitScreen)
        viewModel.event.collect {
            when (it) {
                is WelcomeScreenEvent.OnCreateNewVault -> navigator.push(SetLockPasswordScreen)
                is WelcomeScreenEvent.OnOpenExistingVault -> navigator.push(ImportExportScreen)
                is WelcomeScreenEvent.OnLoadVault -> navigator.push(UnLockVaultScreen)
            }
        }
    }
} 