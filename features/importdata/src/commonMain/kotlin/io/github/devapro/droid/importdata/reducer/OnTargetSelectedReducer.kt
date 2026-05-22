package io.github.devapro.droid.importdata.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.importdata.model.ImportScreenAction
import io.github.devapro.droid.importdata.model.ImportScreenEvent
import io.github.devapro.droid.importdata.model.ImportScreenState

class OnTargetSelectedReducer
    : Reducer<ImportScreenAction.OnTargetSelected, ImportScreenState, ImportScreenAction, ImportScreenEvent> {

    override val actionClass = ImportScreenAction.OnTargetSelected::class

    override suspend fun reduce(
        action: ImportScreenAction.OnTargetSelected,
        getState: () -> ImportScreenState
    ): Reducer.Result<ImportScreenState, ImportScreenAction.OnTargetSelected, ImportScreenEvent?> {
        val current = getState()
        return if (current is ImportScreenState.Loaded) {
            Reducer.Result(state = current.copy(target = action.target), action = null, event = null)
        } else {
            Reducer.Result(state = current, action = null, event = null)
        }
    }
}
