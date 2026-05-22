package io.github.devapro.droid.edit.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.edit.PasswordGenerator
import io.github.devapro.droid.edit.model.AddEditPasswordScreenAction
import io.github.devapro.droid.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.droid.edit.model.AddEditPasswordScreenState

class OnRegenerateGeneratorPreviewReducer
    : Reducer<AddEditPasswordScreenAction.OnRegenerateGeneratorPreview, AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {

    override val actionClass = AddEditPasswordScreenAction.OnRegenerateGeneratorPreview::class

    override suspend fun reduce(
        action: AddEditPasswordScreenAction.OnRegenerateGeneratorPreview,
        getState: () -> AddEditPasswordScreenState
    ): Reducer.Result<AddEditPasswordScreenState, AddEditPasswordScreenAction.OnRegenerateGeneratorPreview, AddEditPasswordScreenEvent?> {
        val currentState = getState()
        return if (currentState is AddEditPasswordScreenState.Success) {
            val preview = PasswordGenerator.generate(currentState.generatorOptions)
            Reducer.Result(
                state = currentState.copy(generatorPreview = preview)
            )
        } else {
            Reducer.Result(state = currentState)
        }
    }
}
