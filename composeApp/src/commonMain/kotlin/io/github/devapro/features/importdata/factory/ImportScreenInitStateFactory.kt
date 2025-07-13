package io.github.devapro.features.importdata.factory

import io.github.devapro.core.mvi.InitStateFactory
import io.github.devapro.features.importdata.model.ImportScreenState

class ImportScreenInitStateFactory : InitStateFactory<ImportScreenState> {
    override fun createInitState(): ImportScreenState {
        return ImportScreenState.Loading
    }
} 