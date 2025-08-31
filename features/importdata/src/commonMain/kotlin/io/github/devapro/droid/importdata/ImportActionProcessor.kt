package io.github.devapro.droid.importdata

import io.github.devapro.droid.core.mvi.ActionProcessor
import io.github.devapro.droid.core.mvi.CoroutineContextProvider
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.importdata.factory.ImportScreenInitStateFactory
import io.github.devapro.droid.importdata.model.ImportScreenAction
import io.github.devapro.droid.importdata.model.ImportScreenEvent
import io.github.devapro.droid.importdata.model.ImportScreenState

class ImportActionProcessor(
    private val initStateFactory: ImportScreenInitStateFactory,
    private val coroutineContextProvider: CoroutineContextProvider,
    reducers: Set<Reducer<ImportScreenAction, ImportScreenState, ImportScreenAction, ImportScreenEvent>>,
) : ActionProcessor<
        ImportScreenState,
        ImportScreenAction,
        ImportScreenEvent>(
    reducers = reducers,
    initStateFactory = initStateFactory,
    coroutineDispatcher = coroutineContextProvider.default
) 