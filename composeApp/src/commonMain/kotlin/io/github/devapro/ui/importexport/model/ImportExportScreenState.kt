package io.github.devapro.ui.importexport.model

sealed interface ImportExportScreenState {
    data object Loading : ImportExportScreenState

    data class Error(val message: String) : ImportExportScreenState

    data class Success(
        val selectedTab: ImportExportTab,
        val selectedFormat: FileFormat,
        val isProcessing: Boolean,
        val formatDescription: String
    ) : ImportExportScreenState
}

enum class ImportExportTab {
    IMPORT, EXPORT
} 