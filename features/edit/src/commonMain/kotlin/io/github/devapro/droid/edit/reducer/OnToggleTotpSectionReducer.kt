package io.github.devapro.droid.edit.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.edit.model.AddEditPasswordScreenAction
import io.github.devapro.droid.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.droid.edit.model.AddEditPasswordScreenState

class OnToggleTotpSectionReducer
    : Reducer<AddEditPasswordScreenAction.OnToggleTotpSection, AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {

    override val actionClass = AddEditPasswordScreenAction.OnToggleTotpSection::class

    override suspend fun reduce(
        action: AddEditPasswordScreenAction.OnToggleTotpSection,
        getState: () -> AddEditPasswordScreenState
    ): Reducer.Result<AddEditPasswordScreenState, AddEditPasswordScreenAction.OnToggleTotpSection, AddEditPasswordScreenEvent?> {
        val currentState = getState()
        return if (currentState is AddEditPasswordScreenState.Success) {
            Reducer.Result(
                state = currentState.copy(isTotpVisible = !currentState.isTotpVisible)
            )
        } else {
            Reducer.Result(state = currentState)
        }
    }
}
