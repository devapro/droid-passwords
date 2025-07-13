package io.github.devapro.features.export.model

import io.github.vinceglb.filekit.PlatformFile

sealed interface ExportScreenAction {
    data object InitScreen : ExportScreenAction

    data class OnFormatSelected(val format: FormatModel) : ExportScreenAction

    data object OnExportClicked : ExportScreenAction

    data class ExportFileSelected(val file: PlatformFile) : ExportScreenAction

    data object ExportFileCancelled : ExportScreenAction

    data object OnBackClicked : ExportScreenAction
}