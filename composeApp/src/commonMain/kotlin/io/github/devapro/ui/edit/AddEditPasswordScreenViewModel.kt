package io.github.devapro.ui.edit

import io.github.devapro.core.mvi.MviViewModel
import io.github.devapro.ui.edit.model.AddEditPasswordScreenAction
import io.github.devapro.ui.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.ui.edit.model.AddEditPasswordScreenState

class AddEditPasswordScreenViewModel(
    actionProcessor: AddEditPasswordScreenActionProcessor
) : MviViewModel<AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent>(
    actionProcessor = actionProcessor
) 