package io.github.devapro.droid.edit.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.edit.model.AddEditPasswordScreenAction
import io.github.devapro.droid.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.droid.edit.model.AddEditPasswordScreenState

class OnPasswordChangedReducer
    : Reducer<AddEditPasswordScreenAction.OnPasswordChanged, AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {

    override val actionClass = AddEditPasswordScreenAction.OnPasswordChanged::class

    override suspend fun reduce(
        action: AddEditPasswordScreenAction.OnPasswordChanged,
        getState: () -> AddEditPasswordScreenState
    ): Reducer.Result<AddEditPasswordScreenState, AddEditPasswordScreenAction.OnPasswordChanged, AddEditPasswordScreenEvent?> {
        val currentState = getState()

        return if (currentState is AddEditPasswordScreenState.Success) {
            val passwordError = if (action.password.isBlank()) "Password is required" else null
            val isFormValid = currentState.title.isNotBlank() && action.password.isNotBlank()
            
            Reducer.Result(
                state = currentState.copy(
                    password = action.password,
                    passwordError = passwordError,
                    isFormValid = isFormValid
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