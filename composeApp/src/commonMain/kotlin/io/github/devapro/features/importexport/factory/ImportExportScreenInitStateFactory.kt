package io.github.devapro.features.importexport.factory

import io.github.devapro.core.mvi.InitStateFactory
import io.github.devapro.features.importexport.model.ImportExportScreenState

class ImportExportScreenInitStateFactory: InitStateFactory<ImportExportScreenState> {
    override fun createInitState(): ImportExportScreenState {
        return ImportExportScreenState.Loading
    }
} 