package io.github.devapro.ui.itemslist

import io.github.devapro.core.mvi.ActionProcessor
import io.github.devapro.core.mvi.CoroutineContextProvider
import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.itemslist.factory.PasswordListScreenInitStateFactory
import io.github.devapro.ui.itemslist.model.PasswordListScreenAction
import io.github.devapro.ui.itemslist.model.PasswordListScreenEvent
import io.github.devapro.ui.itemslist.model.PasswordListScreenState

class PasswordListScreenActionProcessor(
    private val initStateFactory: PasswordListScreenInitStateFactory,
    private val coroutineContextProvider: CoroutineContextProvider,
    reducers: Set<Reducer<PasswordListScreenAction, PasswordListScreenState, PasswordListScreenAction, PasswordListScreenEvent>>,
) : ActionProcessor<
        PasswordListScreenState,
        PasswordListScreenAction,
        PasswordListScreenEvent>(
    reducers = reducers,
    initStateFactory = initStateFactory,
    coroutineDispatcher = coroutineContextProvider.default
) 