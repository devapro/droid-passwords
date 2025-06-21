package io.github.devapro.ui.edit.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.model.AdditionalFieldsModel
import io.github.devapro.ui.edit.model.AddEditPasswordScreenAction
import io.github.devapro.ui.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.ui.edit.model.AddEditPasswordScreenState
import kotlinx.uuid.UUID
import kotlinx.uuid.generateUUID

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
                id = UUID.generateUUID().toString(),
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