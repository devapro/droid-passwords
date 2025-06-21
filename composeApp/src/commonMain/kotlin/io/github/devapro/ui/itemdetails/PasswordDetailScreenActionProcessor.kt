package io.github.devapro.ui.itemdetails

import io.github.devapro.core.mvi.ActionProcessor
import io.github.devapro.core.mvi.CoroutineContextProvider
import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.itemdetails.factory.PasswordDetailScreenInitStateFactory
import io.github.devapro.ui.itemdetails.model.PasswordDetailScreenAction
import io.github.devapro.ui.itemdetails.model.PasswordDetailScreenEvent
import io.github.devapro.ui.itemdetails.model.PasswordDetailScreenState

class PasswordDetailScreenActionProcessor(
    private val initStateFactory: PasswordDetailScreenInitStateFactory,
    private val coroutineContextProvider: CoroutineContextProvider,
    reducers: Set<Reducer<PasswordDetailScreenAction, PasswordDetailScreenState, PasswordDetailScreenAction, PasswordDetailScreenEvent>>,
) : ActionProcessor<
        PasswordDetailScreenState,
        PasswordDetailScreenAction,
        PasswordDetailScreenEvent>(
    reducers = reducers,
    initStateFactory = initStateFactory,
    coroutineDispatcher = coroutineContextProvider.default
) 