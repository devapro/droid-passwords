package io.github.devapro.features.importdata.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.features.importdata.model.ImportScreenAction
import io.github.devapro.features.importdata.model.ImportScreenEvent
import io.github.devapro.features.importdata.model.ImportScreenState

class OnBackClickedReducer
    :
    Reducer<ImportScreenAction.OnBackClicked, ImportScreenState, ImportScreenAction, ImportScreenEvent> {

    override val actionClass = ImportScreenAction.OnBackClicked::class

    override suspend fun reduce(
        action: ImportScreenAction.OnBackClicked,
        getState: () -> ImportScreenState
    ): Reducer.Result<ImportScreenState, ImportScreenAction.OnBackClicked, ImportScreenEvent?> {
        return Reducer.Result(
            state = getState(),
            action = null,
            event = ImportScreenEvent.NavigateBack
        )
    }
} 