package io.github.devapro.ui.importexport.model

sealed interface ImportExportScreenState {
    data object Loading : ImportExportScreenState

    data class Error(val message: String) : ImportExportScreenState

    data class Loaded(
        val selectedTab: ImportExportTab,
        val selectedFormat: FileFormat,
        val isProcessing: Boolean,
        val formats: List<FormatModel>,
        val formatDescription: String
    ) : ImportExportScreenState
}

