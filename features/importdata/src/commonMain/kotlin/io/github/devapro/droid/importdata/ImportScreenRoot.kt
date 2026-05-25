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
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.openFilePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
                        message = it.message,
                        actionButtonText = "OK",
                        duration = SnackbarDuration.Short,
                        actionButtonCallback = { }
                    )
                    navigator.pop()
                }

                is ImportScreenEvent.OpenFileForImport -> {
                    // FileKit's macOS picker calls into AppKit, which may re-enter the AWT EDT
                    // via accessibility callbacks. Running the suspend call on Dispatchers.IO
                    // keeps the EDT free so that AppKit ↔ EDT round-trip can complete.
                    val file = withContext(Dispatchers.IO) {
                        FileKit.openFilePicker(
                            type = it.type,
                            title = "Select File",
                        )
                    }
                    if (file != null) {
                        viewModel.onAction(ImportScreenAction.ImportFileSelected(file = file))
                    } else {
                        viewModel.onAction(ImportScreenAction.ImportFileCancelled)
                    }
                }
            }
        }
    }
}
