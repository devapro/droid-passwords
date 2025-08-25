package io.github.devapro.features.export

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.devapro.droid.core.ui.SnackbarHostStateManager
import io.github.devapro.features.export.model.ExportScreenAction
import io.github.devapro.features.export.model.ExportScreenEvent
import io.github.devapro.features.export.ui.ExportScreenContent
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.openFileSaver
import org.koin.compose.koinInject

@Composable
fun ExportScreenRoot() {
    val viewModel: ExportScreenViewModel = koinInject()
    val snackBarManager: SnackbarHostStateManager = koinInject()

    val navigator = LocalNavigator.currentOrThrow

    val state by viewModel.state.collectAsState()
    ExportScreenContent(
        state = state,
        onAction = viewModel::onAction
    )

    LaunchedEffect(Unit) {
        viewModel.event.collect {
            when (it) {
                is ExportScreenEvent.NavigateBack -> {
                    navigator.pop()
                }

                is ExportScreenEvent.ShowError -> {
                    snackBarManager.show(
                        message = it.message,
                        actionButtonText = "OK",
                        duration = SnackbarDuration.Short,
                        actionButtonCallback = { }
                    )
                }

                is ExportScreenEvent.ShowSuccess -> {
                    snackBarManager.show(
                        message = "Operation completed successfully",
                        actionButtonText = "OK",
                        duration = SnackbarDuration.Short,
                        actionButtonCallback = { }
                    )
                }

                is ExportScreenEvent.OpenFileForExport -> {
                    val file = FileKit.openFileSaver(
                        suggestedName = it.fileName,
                        extension = it.fileExtension
                    )
                    if (file != null) {
                        viewModel.onAction(ExportScreenAction.ExportFileSelected(file))
                    } else {
                        viewModel.onAction(ExportScreenAction.ExportFileCancelled)
                    }
                }
            }
        }
    }

} 