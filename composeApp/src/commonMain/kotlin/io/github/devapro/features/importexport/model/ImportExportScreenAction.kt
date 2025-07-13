package io.github.devapro.features.importexport.model

import io.github.vinceglb.filekit.PlatformFile

sealed interface ImportExportScreenAction {
    data object InitScreen : ImportExportScreenAction

    data object OnSwitchToImport : ImportExportScreenAction
    
    data object OnSwitchToExport : ImportExportScreenAction

    data class OnFormatSelected(val format: FormatModel) : ImportExportScreenAction

    data object OnImportClicked : ImportExportScreenAction

    data object OnExportClicked : ImportExportScreenAction

    data class ExportFileSelected(val file: PlatformFile) : ImportExportScreenAction

    data object ExportFileCancelled : ImportExportScreenAction

    data object OnBackClicked : ImportExportScreenAction

    data class ImportFileSelected(
        val file: PlatformFile,
        val password: String
    ) : ImportExportScreenAction

    data object ImportFileCancelled : ImportExportScreenAction
}