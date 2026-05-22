package io.github.devapro.droid.edit.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.edit.PasswordGenerator
import io.github.devapro.droid.edit.model.AddEditPasswordScreenAction
import io.github.devapro.droid.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.droid.edit.model.AddEditPasswordScreenState

class OnGeneratorOptionsChangedReducer
    : Reducer<AddEditPasswordScreenAction.OnGeneratorOptionsChanged, AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {

    override val actionClass = AddEditPasswordScreenAction.OnGeneratorOptionsChanged::class

    override suspend fun reduce(
        action: AddEditPasswordScreenAction.OnGeneratorOptionsChanged,
        getState: () -> AddEditPasswordScreenState
    ): Reducer.Result<AddEditPasswordScreenState, AddEditPasswordScreenAction.OnGeneratorOptionsChanged, AddEditPasswordScreenEvent?> {
        val currentState = getState()
        return if (currentState is AddEditPasswordScreenState.Success) {
            val preview = PasswordGenerator.generate(action.options)
            Reducer.Result(
                state = currentState.copy(
                    generatorOptions = action.options,
                    generatorPreview = preview
                )
            )
        } else {
            Reducer.Result(state = currentState)
        }
    }
}
