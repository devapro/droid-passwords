package io.github.devapro.features.edit.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.edit.model.AddEditPasswordScreenAction
import io.github.devapro.features.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.features.edit.model.AddEditPasswordScreenState

class OnDeleteClickedReducer
    : Reducer<AddEditPasswordScreenAction.OnDeleteClicked, AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {

    override val actionClass = AddEditPasswordScreenAction.OnDeleteClicked::class

    override suspend fun reduce(
        action: AddEditPasswordScreenAction.OnDeleteClicked,
        getState: () -> AddEditPasswordScreenState
    ): Reducer.Result<AddEditPasswordScreenState, AddEditPasswordScreenAction.OnDeleteClicked, AddEditPasswordScreenEvent?> {
        val currentState = getState()

        return if (currentState is AddEditPasswordScreenState.Success && currentState.isEditMode && currentState.itemId != null) {
            Reducer.Result(
                state = currentState.copy(isSaving = true),
                action = null,
                event = AddEditPasswordScreenEvent.DeleteSuccess(currentState.itemId)
            )
        } else {
            Reducer.Result(
                state = currentState,
                action = null,
                event = AddEditPasswordScreenEvent.DeleteError("Cannot delete item")
            )
        }
    }
} 