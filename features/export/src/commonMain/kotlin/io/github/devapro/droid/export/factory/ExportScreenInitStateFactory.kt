package io.github.devapro.droid.export.factory

import io.github.devapro.droid.core.mvi.InitStateFactory
import io.github.devapro.droid.export.model.ExportScreenState

class ExportScreenInitStateFactory : InitStateFactory<ExportScreenState> {
    override fun createInitState(): ExportScreenState {
        return ExportScreenState.Loading
    }
} 