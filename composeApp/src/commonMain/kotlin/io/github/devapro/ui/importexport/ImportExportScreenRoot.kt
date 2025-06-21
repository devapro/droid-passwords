package io.github.devapro.ui.importexport

import androidx.compose.runtime.*
import io.github.devapro.ui.importexport.ui.ImportExportScreenContent
import org.koin.compose.koinInject

@Composable
fun ImportExportScreenRoot() {
    val viewModel: ImportExportScreenViewModel = koinInject()
    val state by viewModel.state.collectAsState()
    ImportExportScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
} 