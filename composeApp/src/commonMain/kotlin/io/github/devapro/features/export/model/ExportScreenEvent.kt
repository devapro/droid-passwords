package io.github.devapro.features.export.model

import io.github.vinceglb.filekit.dialogs.FileKitType

sealed interface ExportScreenEvent {

    data object NavigateBack : ExportScreenEvent

    data class OpenFileForExport(
        val fileName: String,
        val fileExtension: String
    ) : ExportScreenEvent

    data class OpenFileForImport(val type: FileKitType) : ExportScreenEvent

    data class ShowError(val message: String) : ExportScreenEvent

    data object ShowSuccess : ExportScreenEvent
} 