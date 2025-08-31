package io.github.devapro.droid.importdata.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.importdata.factory.FormatsListFactory
import io.github.devapro.droid.importdata.model.FileFormat
import io.github.devapro.droid.importdata.model.ImportScreenAction
import io.github.devapro.droid.importdata.model.ImportScreenEvent
import io.github.devapro.droid.importdata.model.ImportScreenState

class InitScreenReducer(
    private val formatsListFactory: FormatsListFactory
) : Reducer<ImportScreenAction.InitScreen, ImportScreenState, ImportScreenAction, ImportScreenEvent> {

    override val actionClass = ImportScreenAction.InitScreen::class

    override suspend fun reduce(
        action: ImportScreenAction.InitScreen,
        getState: () -> ImportScreenState
    ): Reducer.Result<ImportScreenState, ImportScreenAction.InitScreen, ImportScreenEvent?> {
        return Reducer.Result(
            state = ImportScreenState.Loaded(
                password = "",
                passwordError = null,
                isValid = false,
                isPasswordVisible = false,
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