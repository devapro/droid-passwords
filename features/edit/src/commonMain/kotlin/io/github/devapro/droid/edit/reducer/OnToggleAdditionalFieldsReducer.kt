package io.github.devapro.droid.edit.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.edit.model.AddEditPasswordScreenAction
import io.github.devapro.droid.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.droid.edit.model.AddEditPasswordScreenState

class OnToggleAdditionalFieldsReducer :
    Reducer<AddEditPasswordScreenAction.OnToggleAdditionalFields, AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {

    override val actionClass = AddEditPasswordScreenAction.OnToggleAdditionalFields::class

    override suspend fun reduce(
        action: AddEditPasswordScreenAction.OnToggleAdditionalFields,
        getState: () -> AddEditPasswordScreenState
    ): Reducer.Result<AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent?> {
        val state = getState()
        return if (state is AddEditPasswordScreenState.Success) {
            Reducer.Result(
                state = state.copy(
                    isAdditionalFieldsVisible = !state.isAdditionalFieldsVisible
                )
            )
        } else {
            Reducer.Result(state = state)
        }
    }
}
