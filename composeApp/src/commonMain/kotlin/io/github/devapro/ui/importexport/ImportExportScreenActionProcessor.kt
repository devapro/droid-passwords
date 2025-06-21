package io.github.devapro.ui.importexport

import io.github.devapro.core.mvi.ActionProcessor
import io.github.devapro.core.mvi.CoroutineContextProvider
import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.importexport.factory.ImportExportScreenInitStateFactory
import io.github.devapro.ui.importexport.model.ImportExportScreenAction
import io.github.devapro.ui.importexport.model.ImportExportScreenEvent
import io.github.devapro.ui.importexport.model.ImportExportScreenState

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