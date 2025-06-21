package io.github.devapro.ui.unlock.factory

import io.github.devapro.core.mvi.InitStateFactory
import io.github.devapro.ui.unlock.model.UnLockVaultScreenState

class UnLockVaultScreenInitStateFactory : InitStateFactory<UnLockVaultScreenState> {

    override fun createInitState(): UnLockVaultScreenState {
        return UnLockVaultScreenState.Loading
    }
} 