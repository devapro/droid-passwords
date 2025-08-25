package io.github.devapro.features.importdata.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.features.importdata.model.ImportScreenAction
import io.github.devapro.features.importdata.model.ImportScreenEvent
import io.github.devapro.features.importdata.model.ImportScreenState

class OnTogglePasswordVisibilityReducer
    :
    Reducer<ImportScreenAction.OnTogglePasswordVisibility, ImportScreenState, ImportScreenAction, ImportScreenEvent> {

    override val actionClass = ImportScreenAction.OnTogglePasswordVisibility::class

    override suspend fun reduce(
        action: ImportScreenAction.OnTogglePasswordVisibility,
        getState: () -> ImportScreenState
    ): Reducer.Result<ImportScreenState, ImportScreenAction, ImportScreenEvent?> {
        val currentState = getState()

        return if (currentState is ImportScreenState.Loaded) {
            Reducer.Result(
                state = currentState.copy(isPasswordVisible = !currentState.isPasswordVisible),
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