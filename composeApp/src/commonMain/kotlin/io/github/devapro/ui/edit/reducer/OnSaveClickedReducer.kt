package io.github.devapro.ui.edit.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.model.ItemModel
import io.github.devapro.ui.edit.model.AddEditPasswordScreenAction
import io.github.devapro.ui.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.ui.edit.model.AddEditPasswordScreenState
import kotlinx.uuid.UUID
import kotlinx.uuid.generateUUID

class OnSaveClickedReducer
    : Reducer<AddEditPasswordScreenAction.OnSaveClicked, AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {

    override val actionClass = AddEditPasswordScreenAction.OnSaveClicked::class

    override suspend fun reduce(
        action: AddEditPasswordScreenAction.OnSaveClicked,
        getState: () -> AddEditPasswordScreenState
    ): Reducer.Result<AddEditPasswordScreenState, AddEditPasswordScreenAction.OnSaveClicked, AddEditPasswordScreenEvent?> {
        val currentState = getState()

        return if (currentState is AddEditPasswordScreenState.Success && currentState.isFormValid) {
            val item = ItemModel(
                id = currentState.itemId ?: UUID.generateUUID().toString(),
                title = currentState.title,
                username = currentState.username,
                password = currentState.password,
                url = currentState.url,
                description = currentState.description,
                additionalFields = currentState.additionalFields
            )
            
            Reducer.Result(
                state = currentState.copy(isSaving = true),
                action = null,
                event = AddEditPasswordScreenEvent.SaveSuccess(item)
            )
        } else {
            Reducer.Result(
                state = currentState,
                action = null,
                event = AddEditPasswordScreenEvent.SaveError("Please fill in all required fields")
            )
        }
    }
} 