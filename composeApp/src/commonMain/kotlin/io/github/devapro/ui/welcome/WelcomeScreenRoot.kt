package io.github.devapro.ui.welcome

import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.devapro.ui.importexport.navigation.ImportExportScreen
import io.github.devapro.ui.itemslist.navigation.PasswordListScreen
import io.github.devapro.ui.setlock.navigation.SetLockPasswordScreen
import io.github.devapro.ui.unlock.navigation.UnLockVaultScreen
import io.github.devapro.ui.welcome.model.WelcomeScreenAction
import io.github.devapro.ui.welcome.model.WelcomeScreenEvent
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

    val navigator = LocalNavigator.currentOrThrow
    LaunchedEffect(Unit) {
        viewModel.onAction(WelcomeScreenAction.InitScreen)
        viewModel.event.collect {
            when (it) {
                is WelcomeScreenEvent.OnCreateNewVault -> navigator.push(PasswordListScreen)
                is WelcomeScreenEvent.OnOpenExistingVault -> navigator.push(ImportExportScreen)
            }
        }
    }
} 