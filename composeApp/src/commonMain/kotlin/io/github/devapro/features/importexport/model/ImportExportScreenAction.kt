package io.github.devapro.features.importexport.model

sealed interface ImportExportScreenAction {
    data object InitScreen : ImportExportScreenAction

    data object OnSwitchToImport : ImportExportScreenAction
    
    data object OnSwitchToExport : ImportExportScreenAction

    data class OnFormatSelected(val format: FormatModel) : ImportExportScreenAction

    data object OnImportClicked : ImportExportScreenAction

    data object OnExportClicked : ImportExportScreenAction

    data object OnBackClicked : ImportExportScreenAction
}