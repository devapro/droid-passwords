package io.github.devapro.droid.importdata.factory

import io.github.devapro.droid.core.mvi.InitStateFactory
import io.github.devapro.droid.importdata.model.ImportScreenState

class ImportScreenInitStateFactory : InitStateFactory<ImportScreenState> {
    override fun createInitState(): ImportScreenState {
        return ImportScreenState.Loading
    }
} 