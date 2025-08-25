package io.github.devapro.features.edit.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.features.edit.model.AddEditPasswordScreenAction
import io.github.devapro.features.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.features.edit.model.AddEditPasswordScreenState

class OnAdditionalFieldValueChangedReducer
    : Reducer<AddEditPasswordScreenAction.OnAdditionalFieldValueChanged, AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {

    override val actionClass = AddEditPasswordScreenAction.OnAdditionalFieldValueChanged::class

    override suspend fun reduce(
        action: AddEditPasswordScreenAction.OnAdditionalFieldValueChanged,
        getState: () -> AddEditPasswordScreenState
    ): Reducer.Result<AddEditPasswordScreenState, AddEditPasswordScreenAction.OnAdditionalFieldValueChanged, AddEditPasswordScreenEvent?> {
        val currentState = getState()

        return if (currentState is AddEditPasswordScreenState.Success) {
            val updatedFields = currentState.additionalFields.toMutableList()
            if (action.index < updatedFields.size) {
                updatedFields[action.index] = updatedFields[action.index].copy(value = action.value)
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