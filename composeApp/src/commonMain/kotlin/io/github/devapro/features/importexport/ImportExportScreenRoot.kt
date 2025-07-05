package io.github.devapro.features.importexport

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import io.github.devapro.features.importexport.ui.ImportExportScreenContent
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