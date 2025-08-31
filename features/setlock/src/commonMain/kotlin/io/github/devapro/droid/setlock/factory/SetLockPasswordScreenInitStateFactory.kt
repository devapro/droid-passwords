package io.github.devapro.droid.setlock.factory

import io.github.devapro.droid.core.mvi.InitStateFactory
import io.github.devapro.droid.setlock.model.SetLockPasswordScreenState

class SetLockPasswordScreenInitStateFactory : InitStateFactory<SetLockPasswordScreenState> {

    override fun createInitState(): SetLockPasswordScreenState {
        return SetLockPasswordScreenState.Loading
    }
} 