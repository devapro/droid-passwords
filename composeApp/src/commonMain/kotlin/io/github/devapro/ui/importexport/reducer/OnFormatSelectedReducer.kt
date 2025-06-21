package io.github.devapro.ui.importexport.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.importexport.model.ImportExportScreenAction
import io.github.devapro.ui.importexport.model.ImportExportScreenEvent
import io.github.devapro.ui.importexport.model.ImportExportScreenState
import io.github.devapro.ui.importexport.model.FileFormat

class OnFormatSelectedReducer
    : Reducer<ImportExportScreenAction.OnFormatSelected, ImportExportScreenState, ImportExportScreenAction, ImportExportScreenEvent> {

    override val actionClass = ImportExportScreenAction.OnFormatSelected::class

    override suspend fun reduce(
        action: ImportExportScreenAction.OnFormatSelected,
        getState: () -> ImportExportScreenState
    ): Reducer.Result<ImportExportScreenState, ImportExportScreenAction.OnFormatSelected, ImportExportScreenEvent?> {
        val currentState = getState()
        return if (currentState is ImportExportScreenState.Success) {
            Reducer.Result(
                state = currentState.copy(
                    selectedFormat = action.format,
                    formatDescription = getFormatDescription(action.format)
                ),
                action = null,
                event = null
            )
        } else {
            Reducer.Result(
                state = currentState,
                action = null,
                event = null
            )
        }
    }

    private fun getFormatDescription(format: FileFormat): String {
        return when (format) {
            FileFormat.CSV -> "Comma-separated values format. Compatible with spreadsheet applications like Excel and Google Sheets. Simple and widely supported."
            FileFormat.XML -> "Extensible Markup Language format. Structured data format that preserves field relationships and is human-readable."
            FileFormat.JSON -> "JavaScript Object Notation format. Lightweight, easy to read, and commonly used for data exchange. Recommended for most use cases."
        }
    }
} 