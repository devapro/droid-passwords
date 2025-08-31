package io.github.devapro.droid.importdata

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.devapro.droid.core.ui.SnackbarHostStateManager
import io.github.devapro.droid.importdata.model.ImportScreenAction
import io.github.devapro.droid.importdata.model.ImportScreenEvent
import io.github.devapro.droid.importdata.ui.ImportScreenContent
import io.github.devapro.droid.tags.navigation.TagsScreen
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.dialogs.openFileSaver
import org.koin.compose.koinInject

@Composable
fun ImportScreenRoot() {
    val viewModel: ImportScreenViewModel = koinInject()
    val snackBarManager: SnackbarHostStateManager = koinInject()

    val navigator = LocalNavigator.currentOrThrow

    val state by viewModel.state.collectAsState()
    ImportScreenContent(
        state = state,
        onAction = viewModel::onAction
    )

    LaunchedEffect(Unit) {
        viewModel.event.collect {
            when (it) {
                is ImportScreenEvent.NavigateBack -> {
                    navigator.pop()
                }

                is ImportScreenEvent.ShowError -> {
                    snackBarManager.show(
                        message = it.message,
                        actionButtonText = "OK",
                        duration = SnackbarDuration.Short,
                        actionButtonCallback = { }
                    )
                }

                is ImportScreenEvent.ShowSuccess -> {
                    snackBarManager.show(
                        message = "Operation completed successfully",
                        actionButtonText = "OK",
                        duration = SnackbarDuration.Short,
                        actionButtonCallback = { }
                    )
                    navigator.replace(TagsScreen)
                }

                is ImportScreenEvent.OpenFileForImport -> {
                    val file = FileKit.openFilePicker(
                        type = it.type,
                        title = "Select File",
                    )
                    if (file != null) {
                        viewModel.onAction(
                            ImportScreenAction.ImportFileSelected(
                                file = file
                            )
                        )
                    } else {
                        viewModel.onAction(ImportScreenAction.ImportFileCancelled)
                    }
                }

                is ImportScreenEvent.OpenFileFor -> {
                    val file = FileKit.openFileSaver(
                        suggestedName = it.fileName,
                        extension = it.fileExtension
                    )
                    if (file != null) {
                        viewModel.onAction(ImportScreenAction.ExportFileSelected(file))
                    } else {
                        viewModel.onAction(ImportScreenAction.ExportFileCancelled)
                    }
                }
            }
        }
    }

} 