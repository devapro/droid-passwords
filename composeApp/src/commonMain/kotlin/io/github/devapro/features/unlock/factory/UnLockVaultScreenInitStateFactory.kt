package io.github.devapro.features.unlock.factory

import io.github.devapro.droid.core.mvi.InitStateFactory
import io.github.devapro.features.unlock.model.UnLockVaultScreenState

class UnLockVaultScreenInitStateFactory : InitStateFactory<UnLockVaultScreenState> {

    override fun createInitState(): UnLockVaultScreenState {
        return UnLockVaultScreenState.Loading
    }
} 