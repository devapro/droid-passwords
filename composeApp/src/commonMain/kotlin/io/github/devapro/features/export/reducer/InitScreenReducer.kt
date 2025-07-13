package io.github.devapro.features.export.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.export.factory.FormatsListFactory
import io.github.devapro.features.export.model.ExportScreenAction
import io.github.devapro.features.export.model.ExportScreenEvent
import io.github.devapro.features.export.model.ExportScreenState
import io.github.devapro.features.export.model.FileFormat

class InitScreenReducer(
    private val formatsListFactory: FormatsListFactory
) : Reducer<ExportScreenAction.InitScreen, ExportScreenState, ExportScreenAction, ExportScreenEvent> {

    override val actionClass = ExportScreenAction.InitScreen::class

    override suspend fun reduce(
        action: ExportScreenAction.InitScreen,
        getState: () -> ExportScreenState
    ): Reducer.Result<ExportScreenState, ExportScreenAction.InitScreen, ExportScreenEvent?> {
        return Reducer.Result(
            state = ExportScreenState.Loaded(
                selectedFormat = FileFormat.JSON,
                isProcessing = false,
                formats = formatsListFactory.createFormatsList(),
                formatDescription = getFormatDescription(FileFormat.JSON)
            ),
            action = null,
            event = null
        )
    }

    private fun getFormatDescription(format: FileFormat): String {
        return when (format) {
            FileFormat.CSV -> "Comma-separated values format. Compatible with spreadsheet applications like Excel and Google Sheets. Simple and widely supported."
            FileFormat.JSON -> "JavaScript Object Notation format. Lightweight, easy to read, and commonly used for data exchange. Recommended for most use cases."
            FileFormat.DATA -> "Custom data format specific to this application. Optimized for internal use and may not be compatible with other applications."
        }
    }
} 