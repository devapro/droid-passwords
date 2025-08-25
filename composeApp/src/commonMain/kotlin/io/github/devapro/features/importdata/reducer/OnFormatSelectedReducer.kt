package io.github.devapro.features.importdata.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.features.importdata.model.ImportScreenAction
import io.github.devapro.features.importdata.model.ImportScreenEvent
import io.github.devapro.features.importdata.model.ImportScreenState

class OnFormatSelectedReducer
    :
    Reducer<ImportScreenAction.OnFormatSelected, ImportScreenState, ImportScreenAction, ImportScreenEvent> {

    override val actionClass = ImportScreenAction.OnFormatSelected::class

    override suspend fun reduce(
        action: ImportScreenAction.OnFormatSelected,
        getState: () -> ImportScreenState
    ): Reducer.Result<ImportScreenState, ImportScreenAction.OnFormatSelected, ImportScreenEvent?> {
        val currentState = getState()
        return if (currentState is ImportScreenState.Loaded) {
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