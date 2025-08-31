package io.github.devapro.droid.importdata.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.importdata.model.ImportScreenAction
import io.github.devapro.droid.importdata.model.ImportScreenEvent
import io.github.devapro.droid.importdata.model.ImportScreenState

class OnPasswordChangedReducer
    :
    Reducer<ImportScreenAction.OnPasswordChanged, ImportScreenState, ImportScreenAction, ImportScreenEvent> {

    override val actionClass = ImportScreenAction.OnPasswordChanged::class

    override suspend fun reduce(
        action: ImportScreenAction.OnPasswordChanged,
        getState: () -> ImportScreenState
    ): Reducer.Result<ImportScreenState, ImportScreenAction, ImportScreenEvent?> {
        val currentState = getState()

        return if (currentState is ImportScreenState.Loaded) {
            val isValid = action.password.isNotEmpty()
            val passwordError =
                if (action.password.isEmpty()) null else null // Clear error on input

            val newState = currentState.copy(
                password = action.password,
                passwordError = passwordError,
                isValid = isValid
            )
            Reducer.Result(
                state = newState,
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