package io.github.devapro.features.itemslist

import io.github.devapro.core.mvi.MviViewModel
import io.github.devapro.features.itemslist.model.PasswordListScreenAction
import io.github.devapro.features.itemslist.model.PasswordListScreenEvent
import io.github.devapro.features.itemslist.model.PasswordListScreenState

class PasswordListScreenViewModel(
    actionProcessor: PasswordListScreenActionProcessor,
) : MviViewModel<PasswordListScreenState, PasswordListScreenAction, PasswordListScreenEvent>(
    actionProcessor = actionProcessor
) 