package io.github.devapro.features.importexport

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.devapro.features.importexport.model.ImportExportScreenEvent
import io.github.devapro.features.importexport.ui.ImportExportScreenContent
import org.koin.compose.koinInject

@Composable
fun ImportExportScreenRoot() {
    val viewModel: ImportExportScreenViewModel = koinInject()
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

                }

                is ImportExportScreenEvent.ShowSuccess -> {

                }

                is ImportExportScreenEvent.ExportFile -> {

                }

                is ImportExportScreenEvent.ImportFile -> {

                }
            }
        }
    }

} 