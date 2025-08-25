package io.github.devapro.features.importdata.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.features.importdata.model.ImportScreenAction
import io.github.devapro.features.importdata.model.ImportScreenEvent
import io.github.devapro.features.importdata.model.ImportScreenState

class OnImportFileCancelledReducer
    :
    Reducer<ImportScreenAction.ImportFileCancelled, ImportScreenState, ImportScreenAction, ImportScreenEvent> {

    override val actionClass = ImportScreenAction.ImportFileCancelled::class

    override suspend fun reduce(
        action: ImportScreenAction.ImportFileCancelled,
        getState: () -> ImportScreenState
    ): Reducer.Result<ImportScreenState, ImportScreenAction, ImportScreenEvent?> {
        return Reducer.Result(
            state = getState(),
            action = null,
            event = ImportScreenEvent.ShowError("File selection cancelled")
        )
    }
} 