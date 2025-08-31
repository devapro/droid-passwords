package io.github.devapro.droid.itemslist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.devapro.droid.data.vault.VaultItemTag
import io.github.devapro.droid.edit.navigation.AddEditPasswordScreen
import io.github.devapro.droid.export.navigation.ExportScreen
import io.github.devapro.droid.itemdetails.navigation.PasswordDetailScreen
import io.github.devapro.droid.itemlist.PasswordTagFilterType
import io.github.devapro.droid.itemslist.model.PasswordListScreenAction
import io.github.devapro.droid.itemslist.model.PasswordListScreenEvent
import io.github.devapro.droid.itemslist.model.PasswordListScreenState
import io.github.devapro.droid.itemslist.ui.PasswordListScreenContent
import io.github.devapro.droid.settings.navigation.SettingsScreen
import org.koin.compose.koinInject

@Composable
fun PasswordListScreenRoot(
    type: PasswordTagFilterType,
    tag: VaultItemTag?
) {
    val viewModel: PasswordListScreenViewModel = koinInject()
    val navigator = LocalNavigator.currentOrThrow
    
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onAction(
            PasswordListScreenAction.InitScreen(
                tagFilterType = type,
                tag = tag
            )
        )
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect {
            when (it) {
                is PasswordListScreenEvent.NavigateToAddPassword -> {
                    navigator.push(AddEditPasswordScreen())
                }
                is PasswordListScreenEvent.NavigateToPasswordDetail -> {
                    navigator.push(PasswordDetailScreen(it.item))
                }
                is PasswordListScreenEvent.NavigateToExport -> {
                    navigator.push(ExportScreen)
                }
                is PasswordListScreenEvent.NavigateToSettings -> {
                    navigator.push(SettingsScreen)
                }
                is PasswordListScreenEvent.DeletePassword -> {
                    // Handle password deletion
                }
                is PasswordListScreenEvent.OnBackClicked -> {
                    navigator.pop()
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