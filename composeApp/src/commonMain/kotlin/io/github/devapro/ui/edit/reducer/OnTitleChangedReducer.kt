package io.github.devapro.ui.edit.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.edit.model.AddEditPasswordScreenAction
import io.github.devapro.ui.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.ui.edit.model.AddEditPasswordScreenState

class OnTitleChangedReducer
    : Reducer<AddEditPasswordScreenAction.OnTitleChanged, AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {

    override val actionClass = AddEditPasswordScreenAction.OnTitleChanged::class

    override suspend fun reduce(
        action: AddEditPasswordScreenAction.OnTitleChanged,
        getState: () -> AddEditPasswordScreenState
    ): Reducer.Result<AddEditPasswordScreenState, AddEditPasswordScreenAction.OnTitleChanged, AddEditPasswordScreenEvent?> {
        val currentState = getState()

        return if (currentState is AddEditPasswordScreenState.Success) {
            val titleError = if (action.title.isBlank()) "Title is required" else null
            val isFormValid = action.title.isNotBlank() && currentState.password.isNotBlank()
            
            Reducer.Result(
                state = currentState.copy(
                    title = action.title,
                    titleError = titleError,
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