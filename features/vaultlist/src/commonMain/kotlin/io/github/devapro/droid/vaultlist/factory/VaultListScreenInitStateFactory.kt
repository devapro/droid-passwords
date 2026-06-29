package io.github.devapro.droid.vaultlist.factory

import io.github.devapro.droid.core.mvi.InitStateFactory
import io.github.devapro.droid.vaultlist.model.VaultListScreenState

class VaultListScreenInitStateFactory : InitStateFactory<VaultListScreenState> {
    override fun createInitState(): VaultListScreenState = VaultListScreenState.Loading
}
