package io.github.devapro.droid.importdata

import io.github.devapro.droid.core.mvi.MviViewModel
import io.github.devapro.droid.importdata.model.ImportScreenAction
import io.github.devapro.droid.importdata.model.ImportScreenEvent
import io.github.devapro.droid.importdata.model.ImportScreenState

class ImportScreenViewModel(
    actionProcessor: ImportActionProcessor
) : MviViewModel<ImportScreenState, ImportScreenAction, ImportScreenEvent>(
    actionProcessor
) 