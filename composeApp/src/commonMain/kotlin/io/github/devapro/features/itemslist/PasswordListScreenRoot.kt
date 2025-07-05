package io.github.devapro.features.itemslist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.devapro.data.vault.VaultItemTag
import io.github.devapro.features.edit.navigation.AddEditPasswordScreen
import io.github.devapro.features.importexport.navigation.ImportExportScreen
import io.github.devapro.features.itemdetails.navigation.PasswordDetailScreen
import io.github.devapro.features.itemslist.model.PasswordListScreenAction
import io.github.devapro.features.itemslist.model.PasswordListScreenEvent
import io.github.devapro.features.itemslist.model.PasswordListScreenState
import io.github.devapro.features.itemslist.navigation.PasswordTagFilterType
import io.github.devapro.features.itemslist.ui.PasswordListScreenContent
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
                is PasswordListScreenEvent.NavigateToImportExport -> {
                    navigator.push(ImportExportScreen)
                }
                is PasswordListScreenEvent.NavigateToSettings -> {
                    //navigator.push(SettingsScreen())
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