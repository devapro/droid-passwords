package io.github.devapro.droid.export

import io.github.devapro.droid.core.mvi.MviViewModel
import io.github.devapro.droid.export.model.ExportScreenAction
import io.github.devapro.droid.export.model.ExportScreenEvent
import io.github.devapro.droid.export.model.ExportScreenState

class ExportScreenViewModel(
    actionProcessor: ExportScreenActionProcessor
) : MviViewModel<ExportScreenState, ExportScreenAction, ExportScreenEvent>(
    actionProcessor
) 