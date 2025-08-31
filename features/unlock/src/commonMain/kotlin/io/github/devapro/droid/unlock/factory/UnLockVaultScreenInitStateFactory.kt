package io.github.devapro.droid.unlock.factory

import io.github.devapro.droid.core.mvi.InitStateFactory
import io.github.devapro.droid.unlock.model.UnLockVaultScreenState

class UnLockVaultScreenInitStateFactory : InitStateFactory<UnLockVaultScreenState> {

    override fun createInitState(): UnLockVaultScreenState {
        return UnLockVaultScreenState.Loading
    }
} 