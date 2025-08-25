package io.github.devapro.features.unlock.factory

import androidx.compose.runtime.Composable
import io.github.devapro.droid.unlock.UnLockVaultScreenFactory
import io.github.devapro.features.unlock.UnLockVaultScreenRoot

class UnLockVaultScreenFactoryImpl: UnLockVaultScreenFactory {

    @Composable
    override fun CreateUnLockVaultScreen() {
        UnLockVaultScreenRoot()
    }
}