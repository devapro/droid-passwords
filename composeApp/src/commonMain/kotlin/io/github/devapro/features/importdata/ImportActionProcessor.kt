package io.github.devapro.features.importdata

import io.github.devapro.core.mvi.ActionProcessor
import io.github.devapro.core.mvi.CoroutineContextProvider
import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.importdata.factory.ImportScreenInitStateFactory
import io.github.devapro.features.importdata.model.ImportScreenAction
import io.github.devapro.features.importdata.model.ImportScreenEvent
import io.github.devapro.features.importdata.model.ImportScreenState

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