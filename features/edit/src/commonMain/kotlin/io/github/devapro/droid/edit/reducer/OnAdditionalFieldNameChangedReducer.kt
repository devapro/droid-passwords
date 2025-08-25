package io.github.devapro.droid.edit.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.edit.model.AddEditPasswordScreenAction
import io.github.devapro.droid.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.droid.edit.model.AddEditPasswordScreenState

class OnAdditionalFieldNameChangedReducer
    : Reducer<AddEditPasswordScreenAction.OnAdditionalFieldNameChanged, AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {

    override val actionClass = AddEditPasswordScreenAction.OnAdditionalFieldNameChanged::class

    override suspend fun reduce(
        action: AddEditPasswordScreenAction.OnAdditionalFieldNameChanged,
        getState: () -> AddEditPasswordScreenState
    ): Reducer.Result<AddEditPasswordScreenState, AddEditPasswordScreenAction.OnAdditionalFieldNameChanged, AddEditPasswordScreenEvent?> {
        val currentState = getState()

        return if (currentState is AddEditPasswordScreenState.Success) {
            val updatedFields = currentState.additionalFields.toMutableList()
            if (action.index < updatedFields.size) {
                updatedFields[action.index] = updatedFields[action.index].copy(name = action.name)
            }
            
            Reducer.Result(
                state = currentState.copy(additionalFields = updatedFields),
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