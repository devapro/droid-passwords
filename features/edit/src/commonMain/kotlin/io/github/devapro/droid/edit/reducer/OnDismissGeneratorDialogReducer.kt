package io.github.devapro.droid.edit.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.edit.model.AddEditPasswordScreenAction
import io.github.devapro.droid.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.droid.edit.model.AddEditPasswordScreenState

class OnDismissGeneratorDialogReducer
    : Reducer<AddEditPasswordScreenAction.OnDismissGeneratorDialog, AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {

    override val actionClass = AddEditPasswordScreenAction.OnDismissGeneratorDialog::class

    override suspend fun reduce(
        action: AddEditPasswordScreenAction.OnDismissGeneratorDialog,
        getState: () -> AddEditPasswordScreenState
    ): Reducer.Result<AddEditPasswordScreenState, AddEditPasswordScreenAction.OnDismissGeneratorDialog, AddEditPasswordScreenEvent?> {
        val currentState = getState()
        return if (currentState is AddEditPasswordScreenState.Success) {
            Reducer.Result(
                state = currentState.copy(
                    showGeneratorDialog = false,
                    generatorPreview = ""
                )
            )
        } else {
            Reducer.Result(state = currentState)
        }
    }
}
