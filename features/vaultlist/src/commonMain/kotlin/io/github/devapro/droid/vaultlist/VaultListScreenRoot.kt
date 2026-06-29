package io.github.devapro.droid.vaultlist

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.devapro.droid.core.ui.SnackbarHostStateManager
import io.github.devapro.droid.importdata.navigation.ImportScreen
import io.github.devapro.droid.setlock.navigation.SetLockPasswordScreen
import io.github.devapro.droid.tags.navigation.TagsScreen
import io.github.devapro.droid.unlock.navigation.UnLockVaultScreen
import io.github.devapro.droid.vaultlist.model.VaultListScreenAction
import io.github.devapro.droid.vaultlist.model.VaultListScreenEvent
import io.github.devapro.droid.vaultlist.ui.VaultListScreenContent
import org.koin.compose.koinInject

@Composable
fun VaultListScreenRoot() {
    val viewModel: VaultListScreenViewModel = koinInject()
    val snackBarManager: SnackbarHostStateManager = koinInject()
    val navigator = LocalNavigator.currentOrThrow

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onAction(VaultListScreenAction.InitScreen)
        viewModel.event.collect { event ->
            when (event) {
                is VaultListScreenEvent.NavigateBack -> navigator.pop()
                is VaultListScreenEvent.NavigateToCreate -> navigator.push(SetLockPasswordScreen)
                is VaultListScreenEvent.NavigateToImport -> navigator.push(ImportScreen)
                is VaultListScreenEvent.NavigateToTags -> {
                    navigator.popUntilRoot()
                    navigator.push(TagsScreen)
                }
                is VaultListScreenEvent.NavigateToUnlock -> {
                    navigator.push(UnLockVaultScreen(event.vaultId))
                }
                is VaultListScreenEvent.ShowError -> {
                    snackBarManager.show(
                        message = event.message,
                        actionButtonText = "OK",
                        duration = SnackbarDuration.Short,
                        actionButtonCallback = { },
                    )
                }
                is VaultListScreenEvent.ShowMessage -> {
                    snackBarManager.show(
                        message = event.message,
                        actionButtonText = "OK",
                        duration = SnackbarDuration.Short,
                        actionButtonCallback = { },
                    )
                }
            }
        }
    }

    VaultListScreenContent(state = state, onAction = viewModel::onAction)
}
