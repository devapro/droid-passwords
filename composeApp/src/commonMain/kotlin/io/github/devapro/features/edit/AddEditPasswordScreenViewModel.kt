package io.github.devapro.features.edit

import io.github.devapro.droid.core.mvi.MviViewModel
import io.github.devapro.features.edit.model.AddEditPasswordScreenAction
import io.github.devapro.features.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.features.edit.model.AddEditPasswordScreenState

class AddEditPasswordScreenViewModel(
    actionProcessor: AddEditPasswordScreenActionProcessor
) : MviViewModel<AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent>(
    actionProcessor = actionProcessor
) 