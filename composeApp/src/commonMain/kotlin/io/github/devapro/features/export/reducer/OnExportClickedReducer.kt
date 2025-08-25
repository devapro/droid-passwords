package io.github.devapro.features.export.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.features.export.model.ExportScreenAction
import io.github.devapro.features.export.model.ExportScreenEvent
import io.github.devapro.features.export.model.ExportScreenState

class OnExportClickedReducer
    :
    Reducer<ExportScreenAction.OnExportClicked, ExportScreenState, ExportScreenAction, ExportScreenEvent> {

    override val actionClass = ExportScreenAction.OnExportClicked::class

    override suspend fun reduce(
        action: ExportScreenAction.OnExportClicked,
        getState: () -> ExportScreenState
    ): Reducer.Result<ExportScreenState, ExportScreenAction, ExportScreenEvent?> {
        val currentState = getState()
        return if (currentState is ExportScreenState.Loaded) {
            Reducer.Result(
                state = currentState.copy(isProcessing = true),
                action = null,
                event = ExportScreenEvent.OpenFileForExport(
                    fileName = "exported_data",
                    fileExtension = currentState.selectedFormat.fileExtension
                )
            )
        } else {
            Reducer.Result(
                state = currentState,
                action = null,
                event = null
            )
        }
    }
} 