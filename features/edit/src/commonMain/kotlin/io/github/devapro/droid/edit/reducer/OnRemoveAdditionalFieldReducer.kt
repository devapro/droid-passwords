package io.github.devapro.droid.edit.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.edit.model.AddEditPasswordScreenAction
import io.github.devapro.droid.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.droid.edit.model.AddEditPasswordScreenState

class OnRemoveAdditionalFieldReducer
    : Reducer<AddEditPasswordScreenAction.OnRemoveAdditionalField, AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {

    override val actionClass = AddEditPasswordScreenAction.OnRemoveAdditionalField::class

    override suspend fun reduce(
        action: AddEditPasswordScreenAction.OnRemoveAdditionalField,
        getState: () -> AddEditPasswordScreenState
    ): Reducer.Result<AddEditPasswordScreenState, AddEditPasswordScreenAction.OnRemoveAdditionalField, AddEditPasswordScreenEvent?> {
        val currentState = getState()

        return if (currentState is AddEditPasswordScreenState.Success) {
            val updatedFields = currentState.additionalFields.toMutableList()
            if (action.index < updatedFields.size) {
                updatedFields.removeAt(action.index)
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