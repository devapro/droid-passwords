package io.github.devapro.droid.itemslist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.transitions.SlideTransition
import io.github.devapro.droid.core.navigation.LocalWideScreenFlag
import io.github.devapro.droid.data.vault.VaultItemTag
import io.github.devapro.droid.edit.navigation.AddEditPasswordScreen
import io.github.devapro.droid.export.navigation.ExportScreen
import io.github.devapro.droid.itemdetails.navigation.PasswordDetailScreen
import io.github.devapro.droid.itemlist.PasswordTagFilterType
import io.github.devapro.droid.itemslist.model.PasswordListScreenAction
import io.github.devapro.droid.itemslist.model.PasswordListScreenEvent
import io.github.devapro.droid.itemslist.model.PasswordListScreenState
import io.github.devapro.droid.itemslist.ui.DetailViewPlaceholderScreen
import io.github.devapro.droid.itemslist.ui.PasswordListScreenContent
import io.github.devapro.droid.settings.navigation.SettingsScreen
import kotlinx.coroutines.flow.Flow
import org.koin.compose.koinInject

@Composable
fun PasswordListScreenRoot(
    type: PasswordTagFilterType,
    tag: VaultItemTag?
) {
    val viewModel: PasswordListScreenViewModel = koinInject()

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onAction(
            PasswordListScreenAction.InitScreen(
                tagFilterType = type,
                tag = tag
            )
        )
    }

    when (state) {
        is PasswordListScreenState.Loading -> {
            // Show loading indicator if needed
        }
        is PasswordListScreenState.Error -> {
            // Show error state if needed
        }
        is PasswordListScreenState.Success -> {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val isWideScreen = LocalWideScreenFlag.current

                if (isWideScreen) {
                    // Master-detail layout for large screens
                    // Create the detail navigator and provide it to the entire layout
                    Navigator(DetailViewPlaceholderScreen) { detailNavigator ->
                        CompositionLocalProvider(
                            LocalDetailNavigator provides detailNavigator
                        ) {
                            // Event handling within the scope where LocalDetailNavigator is provided
                            PasswordListEventHandler(
                                eventFlow = viewModel.event
                            )

                            Row(modifier = Modifier.fillMaxSize()) {
                                // Master pane: Password list
                                Box(modifier = Modifier.weight(1f)) {
                                    PasswordListScreenContent(
                                        state = state as PasswordListScreenState.Success,
                                        onAction = viewModel::onAction
                                    )
                                }

                                // Detail pane: Password details with nested navigator
                                Box(modifier = Modifier.weight(1f)) {
                                    SlideTransition(detailNavigator)
                                }
                            }
                        }
                    }
                } else {
                    // Single-pane layout for small screens
                    CompositionLocalProvider(
                        LocalDetailNavigator provides null
                    ) {
                        // Event handling with null detail navigator (single-pane mode)
                        PasswordListEventHandler(
                            eventFlow = viewModel.event
                        )

                        PasswordListScreenContent(
                            state = state as PasswordListScreenState.Success,
                            onAction = viewModel::onAction
                        )
                    }
                }
            }
        }
    }
}

/**
 * Handles navigation events from the ViewModel.
 * Must be called within the scope where LocalDetailNavigator is provided.
 */
@Composable
private fun PasswordListEventHandler(
    eventFlow: Flow<PasswordListScreenEvent>
) {
    val detailNavigator = LocalDetailNavigator.current
    val mainNavigator = detailNavigator?.parent ?: LocalNavigator.currentOrThrow

    LaunchedEffect(Unit) {
        eventFlow.collect { event ->
            when (event) {
                is PasswordListScreenEvent.NavigateToAddPassword -> {
                    mainNavigator.push(AddEditPasswordScreen(selectedTag = event.tag))
                }
                is PasswordListScreenEvent.NavigateToPasswordDetail -> {
                    // On large screens: push to detail pane
                    // On small screens: push to main navigator (full screen)
                    if (detailNavigator != null) {
                        detailNavigator.replace(DetailViewPlaceholderScreen)
                        detailNavigator.push(PasswordDetailScreen(event.item))
                    } else {
                        mainNavigator.push(PasswordDetailScreen(event.item))
                    }
                }
                is PasswordListScreenEvent.NavigateToExport -> {
                    mainNavigator.push(ExportScreen)
                }
                is PasswordListScreenEvent.NavigateToSettings -> {
                    mainNavigator.push(SettingsScreen)
                }
                is PasswordListScreenEvent.DeletePassword -> {
                    // Handle password deletion
                }
                is PasswordListScreenEvent.OnBackClicked -> {
                    mainNavigator.pop()
                }
            }
        }
    }
} 