package io.github.devapro.features.importexport.model

import io.github.vinceglb.filekit.dialogs.FileKitType

sealed interface ImportExportScreenEvent {

    data object NavigateBack : ImportExportScreenEvent

    data class OpenFileForExport(
        val fileName: String,
        val fileExtension: String
    ) : ImportExportScreenEvent

    data class OpenFileForImport(val type: FileKitType) : ImportExportScreenEvent

    data class ShowError(val message: String) : ImportExportScreenEvent

    data object ShowSuccess : ImportExportScreenEvent
} 