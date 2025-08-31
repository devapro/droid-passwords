package io.github.devapro.droid.itemdetails

import io.github.devapro.droid.core.mvi.ActionProcessor
import io.github.devapro.droid.core.mvi.CoroutineContextProvider
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.itemdetails.factory.PasswordDetailScreenInitStateFactory
import io.github.devapro.droid.itemdetails.model.PasswordDetailScreenAction
import io.github.devapro.droid.itemdetails.model.PasswordDetailScreenEvent
import io.github.devapro.droid.itemdetails.model.PasswordDetailScreenState

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