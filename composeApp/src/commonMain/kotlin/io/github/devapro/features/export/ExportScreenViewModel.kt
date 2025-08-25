package io.github.devapro.features.export

import io.github.devapro.droid.core.mvi.MviViewModel
import io.github.devapro.features.export.model.ExportScreenAction
import io.github.devapro.features.export.model.ExportScreenEvent
import io.github.devapro.features.export.model.ExportScreenState

class ExportScreenViewModel(
    actionProcessor: ExportScreenActionProcessor
) : MviViewModel<ExportScreenState, ExportScreenAction, ExportScreenEvent>(
    actionProcessor
) 