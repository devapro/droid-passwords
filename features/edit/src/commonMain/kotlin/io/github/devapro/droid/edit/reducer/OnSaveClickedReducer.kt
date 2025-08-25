package io.github.devapro.droid.edit.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.edit.model.AddEditPasswordScreenAction
import io.github.devapro.droid.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.droid.edit.model.AddEditPasswordScreenState
import kotlin.time.ExperimentalTime

class OnSaveClickedReducer()
    : Reducer<AddEditPasswordScreenAction.OnSaveClicked, AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {

    override val actionClass = AddEditPasswordScreenAction.OnSaveClicked::class

    @OptIn(ExperimentalTime::class)
    override suspend fun reduce(
        action: AddEditPasswordScreenAction.OnSaveClicked,
        getState: () -> AddEditPasswordScreenState
    ): Reducer.Result<AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {
        val currentState = getState()

        return if (currentState is AddEditPasswordScreenState.Success && currentState.isFormValid) {
            Reducer.Result(
                state = currentState.copy(isSaving = true),
                action = AddEditPasswordScreenAction.OnSave,
                event = null
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