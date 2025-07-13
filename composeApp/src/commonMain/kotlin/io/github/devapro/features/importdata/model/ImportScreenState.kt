package io.github.devapro.features.importdata.model

sealed interface ImportScreenState {
    data object Loading : ImportScreenState

    data class Error(val message: String) : ImportScreenState

    data class Loaded(
        val selectedFormat: FileFormat,
        val isProcessing: Boolean,
        val formats: List<FormatModel>,
        val formatDescription: String
    ) : ImportScreenState
}

