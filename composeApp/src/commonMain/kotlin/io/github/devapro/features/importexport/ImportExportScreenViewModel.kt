package io.github.devapro.features.importexport

import io.github.devapro.core.mvi.MviViewModel
import io.github.devapro.features.importexport.model.ImportExportScreenAction
import io.github.devapro.features.importexport.model.ImportExportScreenEvent
import io.github.devapro.features.importexport.model.ImportExportScreenState

class ImportExportScreenViewModel(
    actionProcessor: ImportExportScreenActionProcessor
) : MviViewModel<ImportExportScreenState, ImportExportScreenAction, ImportExportScreenEvent>(
    actionProcessor
) 