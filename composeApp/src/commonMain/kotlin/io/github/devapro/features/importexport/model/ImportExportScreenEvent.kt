package io.github.devapro.features.importexport.model

sealed interface ImportExportScreenEvent {

    data object NavigateBack : ImportExportScreenEvent

    data class OpenFileSelector(val fileName: String) : ImportExportScreenEvent

    data class ImportFile(val format: FileFormat) : ImportExportScreenEvent

    data class ExportFile(val format: FileFormat) : ImportExportScreenEvent

    data class ShowError(val message: String) : ImportExportScreenEvent

    data object ShowSuccess : ImportExportScreenEvent
} 