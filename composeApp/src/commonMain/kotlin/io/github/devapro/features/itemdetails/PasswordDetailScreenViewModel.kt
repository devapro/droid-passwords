package io.github.devapro.features.itemdetails

import io.github.devapro.core.mvi.MviViewModel
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenAction
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenEvent
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenState

class PasswordDetailScreenViewModel(
    actionProcessor: PasswordDetailScreenActionProcessor,
) : MviViewModel<PasswordDetailScreenState, PasswordDetailScreenAction, PasswordDetailScreenEvent>(
    actionProcessor = actionProcessor
) 