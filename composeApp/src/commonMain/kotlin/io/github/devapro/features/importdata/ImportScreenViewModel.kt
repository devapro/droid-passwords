package io.github.devapro.features.importdata

import io.github.devapro.droid.core.mvi.MviViewModel
import io.github.devapro.features.importdata.model.ImportScreenAction
import io.github.devapro.features.importdata.model.ImportScreenEvent
import io.github.devapro.features.importdata.model.ImportScreenState

class ImportScreenViewModel(
    actionProcessor: ImportActionProcessor
) : MviViewModel<ImportScreenState, ImportScreenAction, ImportScreenEvent>(
    actionProcessor
) 