package io.github.devapro.features.importexport

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.devapro.core.ui.SnackbarHostStateManager
import io.github.devapro.features.importexport.model.ImportExportScreenAction
import io.github.devapro.features.importexport.model.ImportExportScreenEvent
import io.github.devapro.features.importexport.ui.ImportExportScreenContent
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.dialogs.openFileSaver
import org.koin.compose.koinInject

@Composable
fun ImportExportScreenRoot() {
    val viewModel: ImportExportScreenViewModel = koinInject()
    val snackBarManager: SnackbarHostStateManager = koinInject()

    val navigator = LocalNavigator.currentOrThrow

    val state by viewModel.state.collectAsState()
    ImportExportScreenContent(
        state = state,
        onAction = viewModel::onAction
    )

    LaunchedEffect(Unit) {
        viewModel.event.collect {
            when (it) {
                is ImportExportScreenEvent.NavigateBack -> {
                    navigator.pop()
                }

                is ImportExportScreenEvent.ShowError -> {
                    snackBarManager.show(
                        message = it.message,
                        actionButtonText = "OK",
                        duration = SnackbarDuration.Short,
                        actionButtonCallback = { }
                    )
                }

                is ImportExportScreenEvent.ShowSuccess -> {
                    snackBarManager.show(
                        message = "Operation completed successfully",
                        actionButtonText = "OK",
                        duration = SnackbarDuration.Short,
                        actionButtonCallback = { }
                    )
                }

                is ImportExportScreenEvent.OpenFileForImport -> {
                    val file = FileKit.openFilePicker(
                        type = it.type,
                        title = "Select File",
                    )
                    if (file != null) {
                        viewModel.onAction(
                            ImportExportScreenAction.ImportFileSelected(
                                file = file,
                                password = "" //TODO: Implement password input if needed
                            )
                        )
                    } else {
                        viewModel.onAction(ImportExportScreenAction.ImportFileCancelled)
                    }
                }

                is ImportExportScreenEvent.OpenFileForExport -> {
                    val file = FileKit.openFileSaver(
                        suggestedName = it.fileName,
                        extension = it.fileExtension
                    )
                    if (file != null) {
                        viewModel.onAction(ImportExportScreenAction.ExportFileSelected(file))
                    } else {
                        viewModel.onAction(ImportExportScreenAction.ExportFileCancelled)
                    }
                }
            }
        }
    }

} 