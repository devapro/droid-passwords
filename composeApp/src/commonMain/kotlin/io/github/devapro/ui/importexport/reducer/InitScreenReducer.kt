package io.github.devapro.ui.importexport.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.importexport.model.ImportExportScreenAction
import io.github.devapro.ui.importexport.model.ImportExportScreenEvent
import io.github.devapro.ui.importexport.model.ImportExportScreenState
import io.github.devapro.ui.importexport.model.ImportExportTab
import io.github.devapro.ui.importexport.model.FileFormat

class InitScreenReducer
    : Reducer<ImportExportScreenAction.InitScreen, ImportExportScreenState, ImportExportScreenAction, ImportExportScreenEvent> {

    override val actionClass = ImportExportScreenAction.InitScreen::class

    override suspend fun reduce(
        action: ImportExportScreenAction.InitScreen,
        getState: () -> ImportExportScreenState
    ): Reducer.Result<ImportExportScreenState, ImportExportScreenAction.InitScreen, ImportExportScreenEvent?> {
        return Reducer.Result(
            state = ImportExportScreenState.Success(
                selectedTab = ImportExportTab.IMPORT,
                selectedFormat = FileFormat.JSON,
                isProcessing = false,
                formatDescription = getFormatDescription(FileFormat.JSON)
            ),
            action = null,
            event = null
        )
    }

    private fun getFormatDescription(format: FileFormat): String {
        return when (format) {
            FileFormat.CSV -> "Comma-separated values format. Compatible with spreadsheet applications like Excel and Google Sheets. Simple and widely supported."
            FileFormat.XML -> "Extensible Markup Language format. Structured data format that preserves field relationships and is human-readable."
            FileFormat.JSON -> "JavaScript Object Notation format. Lightweight, easy to read, and commonly used for data exchange. Recommended for most use cases."
        }
    }
} 