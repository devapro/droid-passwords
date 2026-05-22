package io.github.devapro.droid.edit.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.edit.model.AddEditPasswordScreenAction
import io.github.devapro.droid.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.droid.edit.model.AddEditPasswordScreenState

class OnTotpSecretChangedReducer
    : Reducer<AddEditPasswordScreenAction.OnTotpSecretChanged, AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {

    override val actionClass = AddEditPasswordScreenAction.OnTotpSecretChanged::class

    override suspend fun reduce(
        action: AddEditPasswordScreenAction.OnTotpSecretChanged,
        getState: () -> AddEditPasswordScreenState
    ): Reducer.Result<AddEditPasswordScreenState, AddEditPasswordScreenAction.OnTotpSecretChanged, AddEditPasswordScreenEvent?> {
        val currentState = getState()
        return if (currentState is AddEditPasswordScreenState.Success) {
            Reducer.Result(
                state = currentState.copy(totpSecret = action.secret)
            )
        } else {
            Reducer.Result(state = currentState)
        }
    }
}
