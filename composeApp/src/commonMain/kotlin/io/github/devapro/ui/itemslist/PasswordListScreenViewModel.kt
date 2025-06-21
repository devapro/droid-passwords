package io.github.devapro.ui.itemslist

import io.github.devapro.core.mvi.MviViewModel
import io.github.devapro.ui.itemslist.model.PasswordListScreenAction
import io.github.devapro.ui.itemslist.model.PasswordListScreenEvent
import io.github.devapro.ui.itemslist.model.PasswordListScreenState

class PasswordListScreenViewModel(
    actionProcessor: PasswordListScreenActionProcessor,
) : MviViewModel<PasswordListScreenState, PasswordListScreenAction, PasswordListScreenEvent>(
    actionProcessor = actionProcessor
) 