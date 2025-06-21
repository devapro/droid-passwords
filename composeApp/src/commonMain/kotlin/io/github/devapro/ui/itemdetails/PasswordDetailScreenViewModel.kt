package io.github.devapro.ui.itemdetails

import io.github.devapro.core.mvi.MviViewModel
import io.github.devapro.ui.itemdetails.model.PasswordDetailScreenAction
import io.github.devapro.ui.itemdetails.model.PasswordDetailScreenEvent
import io.github.devapro.ui.itemdetails.model.PasswordDetailScreenState

class PasswordDetailScreenViewModel(
    actionProcessor: PasswordDetailScreenActionProcessor,
) : MviViewModel<PasswordDetailScreenState, PasswordDetailScreenAction, PasswordDetailScreenEvent>(
    actionProcessor = actionProcessor
) 