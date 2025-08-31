package io.github.devapro.droid.export.model

sealed interface ExportScreenState {
    data object Loading : ExportScreenState

    data class Error(val message: String) : ExportScreenState

    data class Loaded(
        val selectedFormat: FileFormat,
        val isProcessing: Boolean,
        val formats: List<FormatModel>,
        val formatDescription: String
    ) : ExportScreenState
}

