package io.github.devapro.droid.edit.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.model.AdditionalFieldsModel
import io.github.devapro.droid.edit.model.AddEditPasswordScreenAction
import io.github.devapro.droid.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.droid.edit.model.AddEditPasswordScreenState

class OnAddAdditionalFieldReducer
    : Reducer<AddEditPasswordScreenAction.OnAddAdditionalField, AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {

    override val actionClass = AddEditPasswordScreenAction.OnAddAdditionalField::class

    override suspend fun reduce(
        action: AddEditPasswordScreenAction.OnAddAdditionalField,
        getState: () -> AddEditPasswordScreenState
    ): Reducer.Result<AddEditPasswordScreenState, AddEditPasswordScreenAction.OnAddAdditionalField, AddEditPasswordScreenEvent?> {
        val currentState = getState()

        return if (currentState is AddEditPasswordScreenState.Success) {
            val newField = AdditionalFieldsModel(
                name = "",
                value = ""
            )
            val updatedFields = currentState.additionalFields + newField
            
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