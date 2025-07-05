package io.github.devapro.features.edit.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.edit.model.AddEditPasswordScreenAction
import io.github.devapro.features.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.features.edit.model.AddEditPasswordScreenState

class OnTagInputChangedReducer :
    Reducer<AddEditPasswordScreenAction.OnTagInputChanged, AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {

    override val actionClass = AddEditPasswordScreenAction.OnTagInputChanged::class

    override suspend fun reduce(
        action: AddEditPasswordScreenAction.OnTagInputChanged,
        getState: () -> AddEditPasswordScreenState
    ): Reducer.Result<AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent?> {
        val state = getState()
        return if (state is AddEditPasswordScreenState.Success) {
            Reducer.Result(
                state = state.copy(
                    tagInput = action.input
                )
            )
        } else {
            Reducer.Result(state = state)
        }
    }
}