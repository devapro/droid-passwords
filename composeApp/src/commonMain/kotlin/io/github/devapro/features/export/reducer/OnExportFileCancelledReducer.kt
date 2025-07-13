package io.github.devapro.features.export.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.export.model.ExportScreenAction
import io.github.devapro.features.export.model.ExportScreenEvent
import io.github.devapro.features.export.model.ExportScreenState

class OnExportFileCancelledReducer
    :
    Reducer<ExportScreenAction.ExportFileCancelled, ExportScreenState, ExportScreenAction, ExportScreenEvent> {

    override val actionClass = ExportScreenAction.ExportFileCancelled::class

    override suspend fun reduce(
        action: ExportScreenAction.ExportFileCancelled,
        getState: () -> ExportScreenState
    ): Reducer.Result<ExportScreenState, ExportScreenAction, ExportScreenEvent?> {
        return Reducer.Result(
            state = getState(),
            action = null,
            event = ExportScreenEvent.ShowError("File selection cancelled")
        )
    }
} 