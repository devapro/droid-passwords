package io.github.devapro.features.setlock.factory

import io.github.devapro.core.mvi.InitStateFactory
import io.github.devapro.features.setlock.model.SetLockPasswordScreenState

class SetLockPasswordScreenInitStateFactory : InitStateFactory<SetLockPasswordScreenState> {

    override fun createInitState(): SetLockPasswordScreenState {
        return SetLockPasswordScreenState.Loading
    }
} 