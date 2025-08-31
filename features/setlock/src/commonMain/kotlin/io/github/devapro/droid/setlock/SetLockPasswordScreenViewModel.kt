package io.github.devapro.droid.setlock

import io.github.devapro.droid.core.mvi.MviViewModel
import io.github.devapro.droid.setlock.model.SetLockPasswordScreenAction
import io.github.devapro.droid.setlock.model.SetLockPasswordScreenEvent
import io.github.devapro.droid.setlock.model.SetLockPasswordScreenState

class SetLockPasswordScreenViewModel(
    actionProcessor: SetLockPasswordScreenActionProcessor
) : MviViewModel<SetLockPasswordScreenState, SetLockPasswordScreenAction, SetLockPasswordScreenEvent>(
    actionProcessor = actionProcessor
) 