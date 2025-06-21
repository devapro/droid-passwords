package io.github.devapro.ui.importexport.model

sealed interface ImportExportScreenEvent {

    data object NavigateBack : ImportExportScreenEvent

    data class ImportFile(val format: FileFormat) : ImportExportScreenEvent

    data class ExportFile(val format: FileFormat) : ImportExportScreenEvent

    data class ShowError(val message: String) : ImportExportScreenEvent

    data object ShowSuccess : ImportExportScreenEvent
} 