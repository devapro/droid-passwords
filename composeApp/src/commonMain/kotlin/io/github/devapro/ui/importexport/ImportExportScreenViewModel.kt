package io.github.devapro.ui.importexport

import io.github.devapro.core.mvi.MviViewModel
import io.github.devapro.ui.importexport.model.ImportExportScreenAction
import io.github.devapro.ui.importexport.model.ImportExportScreenEvent
import io.github.devapro.ui.importexport.model.ImportExportScreenState

class ImportExportScreenViewModel(
    actionProcessor: ImportExportScreenActionProcessor
): MviViewModel<ImportExportScreenState, ImportExportScreenAction, ImportExportScreenEvent>(
    actionProcessor
) 