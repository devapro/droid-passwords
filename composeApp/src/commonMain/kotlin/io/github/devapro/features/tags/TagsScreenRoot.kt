package io.github.devapro.features.tags

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.devapro.features.itemslist.navigation.PasswordListScreen
import io.github.devapro.features.itemslist.navigation.PasswordTagFilterType
import io.github.devapro.features.tags.model.TagItemType
import io.github.devapro.features.tags.model.TagsScreenAction
import io.github.devapro.features.tags.model.TagsScreenEvent
import io.github.devapro.features.tags.model.TagsScreenState
import io.github.devapro.features.tags.ui.TagsScreenContent
import org.koin.compose.koinInject

@Composable
fun TagsScreenRoot() {
    val viewModel: TagsScreenViewModel = koinInject()
    val navigator = LocalNavigator.currentOrThrow

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onAction(TagsScreenAction.InitScreen)
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect {
            when (it) {
                is TagsScreenEvent.NavigateBack -> {
                    navigator.pop()
                }

                is TagsScreenEvent.NavigateToTagDetail -> {
                    navigator.push(
                        PasswordListScreen(
                            type = when (it.tag.type) {
                                TagItemType.ALL -> PasswordTagFilterType.ALL
                                TagItemType.NO_TAGS -> PasswordTagFilterType.NO_TAG
                                else -> PasswordTagFilterType.NORMAL
                            },
                            tag = it.tag.tag
                        )
                    )
                }

                is TagsScreenEvent.RefreshTagsList -> {
                    // Handle refresh
                }

                is TagsScreenEvent.ShowMessage -> {
                    // Show snackbar or toast
                }
            }
        }
    }

    when (state) {
        is TagsScreenState.Loading -> {
            // Show loading indicator if needed
        }

        is TagsScreenState.Error -> {
            // Show error state if needed
        }

        is TagsScreenState.Success -> {
            TagsScreenContent(
                state = state as TagsScreenState.Success,
                onAction = viewModel::onAction
            )
        }
    }
} 