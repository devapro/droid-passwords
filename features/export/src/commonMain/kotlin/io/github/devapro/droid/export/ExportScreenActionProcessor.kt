package io.github.devapro.droid.export

import io.github.devapro.droid.core.mvi.ActionProcessor
import io.github.devapro.droid.core.mvi.CoroutineContextProvider
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.export.factory.ExportScreenInitStateFactory
import io.github.devapro.droid.export.model.ExportScreenAction
import io.github.devapro.droid.export.model.ExportScreenEvent
import io.github.devapro.droid.export.model.ExportScreenState

class ExportScreenActionProcessor(
    private val initStateFactory: ExportScreenInitStateFactory,
    private val coroutineContextProvider: CoroutineContextProvider,
    reducers: Set<Reducer<ExportScreenAction, ExportScreenState, ExportScreenAction, ExportScreenEvent>>,
) : ActionProcessor<
        ExportScreenState,
        ExportScreenAction,
        ExportScreenEvent>(
    reducers = reducers,
    initStateFactory = initStateFactory,
    coroutineDispatcher = coroutineContextProvider.default
) 