package io.github.devapro.droid.edit.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.edit.model.AddEditPasswordScreenAction
import io.github.devapro.droid.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.droid.edit.model.AddEditPasswordScreenState

class OnAcceptGeneratedPasswordReducer
    : Reducer<AddEditPasswordScreenAction.OnAcceptGeneratedPassword, AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {

    override val actionClass = AddEditPasswordScreenAction.OnAcceptGeneratedPassword::class

    override suspend fun reduce(
        action: AddEditPasswordScreenAction.OnAcceptGeneratedPassword,
        getState: () -> AddEditPasswordScreenState
    ): Reducer.Result<AddEditPasswordScreenState, AddEditPasswordScreenAction.OnAcceptGeneratedPassword, AddEditPasswordScreenEvent?> {
        val currentState = getState()
        return if (currentState is AddEditPasswordScreenState.Success && currentState.generatorPreview.isNotEmpty()) {
            val newPassword = currentState.generatorPreview
            val isFormValid = currentState.title.isNotBlank() && newPassword.isNotBlank()
            Reducer.Result(
                state = currentState.copy(
                    password = newPassword,
                    passwordError = null,
                    isFormValid = isFormValid,
                    showGeneratorDialog = false,
                    generatorPreview = ""
                ),
                event = AddEditPasswordScreenEvent.GeneratedPassword(newPassword)
            )
        } else {
            Reducer.Result(state = currentState)
        }
    }
}
