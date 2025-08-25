package io.github.devapro.droid.edit.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.edit.model.AddEditPasswordScreenAction
import io.github.devapro.droid.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.droid.edit.model.AddEditPasswordScreenState

class OnDescriptionChangedReducer
    : Reducer<AddEditPasswordScreenAction.OnDescriptionChanged, AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {

    override val actionClass = AddEditPasswordScreenAction.OnDescriptionChanged::class

    override suspend fun reduce(
        action: AddEditPasswordScreenAction.OnDescriptionChanged,
        getState: () -> AddEditPasswordScreenState
    ): Reducer.Result<AddEditPasswordScreenState, AddEditPasswordScreenAction.OnDescriptionChanged, AddEditPasswordScreenEvent?> {
        val currentState = getState()

        return if (currentState is AddEditPasswordScreenState.Success) {
            Reducer.Result(
                state = currentState.copy(description = action.description),
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