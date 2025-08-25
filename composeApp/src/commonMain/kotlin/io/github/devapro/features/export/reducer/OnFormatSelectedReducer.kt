package io.github.devapro.features.export.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.features.export.model.ExportScreenAction
import io.github.devapro.features.export.model.ExportScreenEvent
import io.github.devapro.features.export.model.ExportScreenState

class OnFormatSelectedReducer
    :
    Reducer<ExportScreenAction.OnFormatSelected, ExportScreenState, ExportScreenAction, ExportScreenEvent> {

    override val actionClass = ExportScreenAction.OnFormatSelected::class

    override suspend fun reduce(
        action: ExportScreenAction.OnFormatSelected,
        getState: () -> ExportScreenState
    ): Reducer.Result<ExportScreenState, ExportScreenAction.OnFormatSelected, ExportScreenEvent?> {
        val currentState = getState()
        return if (currentState is ExportScreenState.Loaded) {
            Reducer.Result(
                state = currentState.copy(
                    selectedFormat = action.format.format,
                    formatDescription = action.format.description
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
} 