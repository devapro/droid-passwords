package io.github.devapro.droid.edit

import io.github.devapro.droid.core.mvi.MviViewModel
import io.github.devapro.droid.edit.model.AddEditPasswordScreenAction
import io.github.devapro.droid.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.droid.edit.model.AddEditPasswordScreenState

class AddEditPasswordScreenViewModel(
    actionProcessor: AddEditPasswordScreenActionProcessor
) : MviViewModel<AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent>(
    actionProcessor = actionProcessor
) 