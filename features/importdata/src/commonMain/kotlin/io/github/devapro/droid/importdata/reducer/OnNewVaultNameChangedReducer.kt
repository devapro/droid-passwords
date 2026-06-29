package io.github.devapro.droid.importdata.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.importdata.model.ImportScreenAction
import io.github.devapro.droid.importdata.model.ImportScreenEvent
import io.github.devapro.droid.importdata.model.ImportScreenState

class OnNewVaultNameChangedReducer
    : Reducer<ImportScreenAction.OnNewVaultNameChanged, ImportScreenState, ImportScreenAction, ImportScreenEvent> {

    override val actionClass = ImportScreenAction.OnNewVaultNameChanged::class

    override suspend fun reduce(
        action: ImportScreenAction.OnNewVaultNameChanged,
        getState: () -> ImportScreenState
    ): Reducer.Result<ImportScreenState, ImportScreenAction.OnNewVaultNameChanged, ImportScreenEvent?> {
        val current = getState()
        return if (current is ImportScreenState.Loaded) {
            Reducer.Result(state = current.copy(newVaultName = action.name), action = null, event = null)
        } else {
            Reducer.Result(state = current, action = null, event = null)
        }
    }
}
