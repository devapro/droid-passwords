package io.github.devapro.features.itemdetails.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenAction
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenEvent
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenState

class OnTogglePasswordVisibilityReducer
    : Reducer<PasswordDetailScreenAction.OnTogglePasswordVisibility, PasswordDetailScreenState, PasswordDetailScreenAction, PasswordDetailScreenEvent> {

    override val actionClass = PasswordDetailScreenAction.OnTogglePasswordVisibility::class

    override suspend fun reduce(
        action: PasswordDetailScreenAction.OnTogglePasswordVisibility,
        getState: () -> PasswordDetailScreenState
    ): Reducer.Result<PasswordDetailScreenState, PasswordDetailScreenAction.OnTogglePasswordVisibility, PasswordDetailScreenEvent?> {
        val currentState = getState()

        return if (currentState is PasswordDetailScreenState.Success) {
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