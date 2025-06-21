package io.github.devapro.ui.setlock.factory

import io.github.devapro.core.mvi.InitStateFactory
import io.github.devapro.ui.setlock.model.SetLockPasswordScreenState

class SetLockPasswordScreenInitStateFactory : InitStateFactory<SetLockPasswordScreenState> {

    override fun createInitState(): SetLockPasswordScreenState {
        return SetLockPasswordScreenState.Loading
    }
} 