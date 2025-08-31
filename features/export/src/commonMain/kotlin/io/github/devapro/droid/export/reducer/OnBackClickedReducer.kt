package io.github.devapro.droid.export.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.export.model.ExportScreenAction
import io.github.devapro.droid.export.model.ExportScreenEvent
import io.github.devapro.droid.export.model.ExportScreenState

class OnBackClickedReducer
    :
    Reducer<ExportScreenAction.OnBackClicked, ExportScreenState, ExportScreenAction, ExportScreenEvent> {

    override val actionClass = ExportScreenAction.OnBackClicked::class

    override suspend fun reduce(
        action: ExportScreenAction.OnBackClicked,
        getState: () -> ExportScreenState
    ): Reducer.Result<ExportScreenState, ExportScreenAction.OnBackClicked, ExportScreenEvent?> {
        return Reducer.Result(
            state = getState(),
            action = null,
            event = ExportScreenEvent.NavigateBack
        )
    }
} 