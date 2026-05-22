package io.github.devapro.droid.edit.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.edit.PasswordGenerator
import io.github.devapro.droid.edit.model.AddEditPasswordScreenAction
import io.github.devapro.droid.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.droid.edit.model.AddEditPasswordScreenState

class OnGeneratePasswordReducer
    : Reducer<AddEditPasswordScreenAction.OnGeneratePassword, AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {

    override val actionClass = AddEditPasswordScreenAction.OnGeneratePassword::class

    override suspend fun reduce(
        action: AddEditPasswordScreenAction.OnGeneratePassword,
        getState: () -> AddEditPasswordScreenState
    ): Reducer.Result<AddEditPasswordScreenState, AddEditPasswordScreenAction.OnGeneratePassword, AddEditPasswordScreenEvent?> {
        val currentState = getState()

        return if (currentState is AddEditPasswordScreenState.Success) {
            val preview = PasswordGenerator.generate(currentState.generatorOptions)
            Reducer.Result(
                state = currentState.copy(
                    showGeneratorDialog = true,
                    generatorPreview = preview
                ),
                action = null,
                event = null
            )
        } else {
            Reducer.Result(
                state = currentState,
                action = null,
                event = null
            )
        }
    }
}
