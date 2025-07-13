package io.github.devapro.features.export.model

sealed interface ExportScreenEvent {

    data object NavigateBack : ExportScreenEvent

    data class OpenFileForExport(
        val fileName: String,
        val fileExtension: String
    ) : ExportScreenEvent

    data class ShowError(val message: String) : ExportScreenEvent

    data object ShowSuccess : ExportScreenEvent
} 