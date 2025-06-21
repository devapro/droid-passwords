package io.github.devapro.ui.edit.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.edit.model.AddEditPasswordScreenAction
import io.github.devapro.ui.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.ui.edit.model.AddEditPasswordScreenState

class OnBackClickedReducer
    : Reducer<AddEditPasswordScreenAction.OnBackClicked, AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {

    override val actionClass = AddEditPasswordScreenAction.OnBackClicked::class

    override suspend fun reduce(
        action: AddEditPasswordScreenAction.OnBackClicked,
        getState: () -> AddEditPasswordScreenState
    ): Reducer.Result<AddEditPasswordScreenState, AddEditPasswordScreenAction.OnBackClicked, AddEditPasswordScreenEvent?> {
        return Reducer.Result(
            state = getState(),
            action = null,
            event = AddEditPasswordScreenEvent.NavigateBack
        )
    }
} 