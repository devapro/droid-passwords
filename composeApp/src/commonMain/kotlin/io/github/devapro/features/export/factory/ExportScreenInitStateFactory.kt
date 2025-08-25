package io.github.devapro.features.export.factory

import io.github.devapro.droid.core.mvi.InitStateFactory
import io.github.devapro.features.export.model.ExportScreenState

class ExportScreenInitStateFactory : InitStateFactory<ExportScreenState> {
    override fun createInitState(): ExportScreenState {
        return ExportScreenState.Loading
    }
} 