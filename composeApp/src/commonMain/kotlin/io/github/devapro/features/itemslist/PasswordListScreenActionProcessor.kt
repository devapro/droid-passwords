package io.github.devapro.features.itemslist

import io.github.devapro.droid.core.mvi.ActionProcessor
import io.github.devapro.droid.core.mvi.CoroutineContextProvider
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.features.itemslist.factory.PasswordListScreenInitStateFactory
import io.github.devapro.features.itemslist.model.PasswordListScreenAction
import io.github.devapro.features.itemslist.model.PasswordListScreenEvent
import io.github.devapro.features.itemslist.model.PasswordListScreenState

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