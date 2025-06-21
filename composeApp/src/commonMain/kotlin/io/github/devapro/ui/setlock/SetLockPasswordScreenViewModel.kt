package io.github.devapro.ui.setlock

import io.github.devapro.core.mvi.MviViewModel
import io.github.devapro.ui.setlock.factory.SetLockPasswordScreenInitStateFactory
import io.github.devapro.ui.setlock.model.SetLockPasswordScreenAction
import io.github.devapro.ui.setlock.model.SetLockPasswordScreenEvent
import io.github.devapro.ui.setlock.model.SetLockPasswordScreenState

class SetLockPasswordScreenViewModel(
    actionProcessor: SetLockPasswordScreenActionProcessor
) : MviViewModel<SetLockPasswordScreenState, SetLockPasswordScreenAction, SetLockPasswordScreenEvent>(
    actionProcessor = actionProcessor
) 