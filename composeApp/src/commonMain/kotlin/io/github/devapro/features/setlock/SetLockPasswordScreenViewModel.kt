package io.github.devapro.features.setlock

import io.github.devapro.core.mvi.MviViewModel
import io.github.devapro.features.setlock.model.SetLockPasswordScreenAction
import io.github.devapro.features.setlock.model.SetLockPasswordScreenEvent
import io.github.devapro.features.setlock.model.SetLockPasswordScreenState

class SetLockPasswordScreenViewModel(
    actionProcessor: SetLockPasswordScreenActionProcessor
) : MviViewModel<SetLockPasswordScreenState, SetLockPasswordScreenAction, SetLockPasswordScreenEvent>(
    actionProcessor = actionProcessor
) 