package io.github.devapro.features.importdata.model

import io.github.vinceglb.filekit.PlatformFile

sealed interface ImportScreenAction {
    data object InitScreen : ImportScreenAction

    data class OnFormatSelected(val format: FormatModel) : ImportScreenAction

    data object OnImportClicked : ImportScreenAction

    data class ExportFileSelected(val file: PlatformFile) : ImportScreenAction

    data object ExportFileCancelled : ImportScreenAction

    data object OnBackClicked : ImportScreenAction

    data class ImportFileSelected(
        val file: PlatformFile,
        val password: String
    ) : ImportScreenAction

    data object ImportFileCancelled : ImportScreenAction
}