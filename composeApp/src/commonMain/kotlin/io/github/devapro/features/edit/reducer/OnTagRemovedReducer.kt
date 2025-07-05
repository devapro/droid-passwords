package io.github.devapro.features.edit.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.edit.model.AddEditPasswordScreenAction
import io.github.devapro.features.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.features.edit.model.AddEditPasswordScreenState

class OnTagRemovedReducer :
    Reducer<AddEditPasswordScreenAction.OnTagRemoved, AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {

    override val actionClass = AddEditPasswordScreenAction.OnTagRemoved::class

    override suspend fun reduce(
        action: AddEditPasswordScreenAction.OnTagRemoved,
        getState: () -> AddEditPasswordScreenState
    ): Reducer.Result<AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent?> {
        val state = getState()
        return if (state is AddEditPasswordScreenState.Success) {
            val newTags = state.tags.toMutableList().apply {
                remove(action.tag)
            }
            Reducer.Result(
                state = state.copy(
                    tags = newTags
                )
            )
        } else {
            Reducer.Result(state = state)
        }
    }
}