package io.github.devapro.features.export

import io.github.devapro.core.mvi.ActionProcessor
import io.github.devapro.core.mvi.CoroutineContextProvider
import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.export.factory.ExportScreenInitStateFactory
import io.github.devapro.features.export.model.ExportScreenAction
import io.github.devapro.features.export.model.ExportScreenEvent
import io.github.devapro.features.export.model.ExportScreenState

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