package io.github.devapro.features.importexport

import io.github.devapro.core.mvi.ActionProcessor
import io.github.devapro.core.mvi.CoroutineContextProvider
import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.importexport.factory.ImportExportScreenInitStateFactory
import io.github.devapro.features.importexport.model.ImportExportScreenAction
import io.github.devapro.features.importexport.model.ImportExportScreenEvent
import io.github.devapro.features.importexport.model.ImportExportScreenState

class ImportExportScreenActionProcessor(
    private val initStateFactory: ImportExportScreenInitStateFactory,
    private val coroutineContextProvider: CoroutineContextProvider,
    reducers: Set<Reducer<ImportExportScreenAction, ImportExportScreenState, ImportExportScreenAction, ImportExportScreenEvent>>,
) : ActionProcessor<
        ImportExportScreenState,
        ImportExportScreenAction,
        ImportExportScreenEvent>(
    reducers = reducers,
    initStateFactory = initStateFactory,
    coroutineDispatcher = coroutineContextProvider.default
) 