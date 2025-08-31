package io.github.devapro.droid.itemslist

import io.github.devapro.droid.core.mvi.MviViewModel
import io.github.devapro.droid.itemslist.model.PasswordListScreenAction
import io.github.devapro.droid.itemslist.model.PasswordListScreenEvent
import io.github.devapro.droid.itemslist.model.PasswordListScreenState

class PasswordListScreenViewModel(
    actionProcessor: PasswordListScreenActionProcessor,
) : MviViewModel<PasswordListScreenState, PasswordListScreenAction, PasswordListScreenEvent>(
    actionProcessor = actionProcessor
) 