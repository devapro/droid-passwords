package io.github.devapro.droid.itemdetails

import io.github.devapro.droid.core.mvi.MviViewModel
import io.github.devapro.droid.itemdetails.model.PasswordDetailScreenAction
import io.github.devapro.droid.itemdetails.model.PasswordDetailScreenEvent
import io.github.devapro.droid.itemdetails.model.PasswordDetailScreenState

class PasswordDetailScreenViewModel(
    actionProcessor: PasswordDetailScreenActionProcessor,
) : MviViewModel<PasswordDetailScreenState, PasswordDetailScreenAction, PasswordDetailScreenEvent>(
    actionProcessor = actionProcessor
) 