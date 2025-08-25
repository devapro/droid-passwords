package io.github.devapro.features.edit.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.features.edit.model.AddEditPasswordScreenAction
import io.github.devapro.features.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.features.edit.model.AddEditPasswordScreenState

class OnTagSelectedReducer :
    Reducer<AddEditPasswordScreenAction.OnTagSelected, AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {

    override val actionClass = AddEditPasswordScreenAction.OnTagSelected::class

    override suspend fun reduce(
        action: AddEditPasswordScreenAction.OnTagSelected,
        getState: () -> AddEditPasswordScreenState
    ): Reducer.Result<AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent?> {
        val state = getState()
        return if (state is AddEditPasswordScreenState.Success) {
            val newTags = state.tags.toMutableList().apply {
                if (!contains(action.tag)) {
                    add(action.tag)
                }
            }
            Reducer.Result(
                state = state.copy(
                    tags = newTags,
                    tagInput = ""
                )
            )
        } else {
            Reducer.Result(state = state)
        }
    }
}