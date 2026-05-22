package io.github.devapro.droid.importdata.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.importdata.model.ImportScreenAction
import io.github.devapro.droid.importdata.model.ImportScreenEvent
import io.github.devapro.droid.importdata.model.ImportScreenState

class OnStrategySelectedReducer
    : Reducer<ImportScreenAction.OnStrategySelected, ImportScreenState, ImportScreenAction, ImportScreenEvent> {

    override val actionClass = ImportScreenAction.OnStrategySelected::class

    override suspend fun reduce(
        action: ImportScreenAction.OnStrategySelected,
        getState: () -> ImportScreenState
    ): Reducer.Result<ImportScreenState, ImportScreenAction.OnStrategySelected, ImportScreenEvent?> {
        val current = getState()
        return if (current is ImportScreenState.Loaded) {
            Reducer.Result(state = current.copy(strategy = action.strategy), action = null, event = null)
        } else {
            Reducer.Result(state = current, action = null, event = null)
        }
    }
}
