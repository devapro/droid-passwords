package io.github.devapro.droid.edit.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.edit.model.AddEditPasswordScreenAction
import io.github.devapro.droid.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.droid.edit.model.AddEditPasswordScreenState

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