package io.github.devapro.droid.unlock.factory

import androidx.compose.runtime.Composable
import io.github.devapro.droid.unlock.UnLockVaultScreenFactory
import io.github.devapro.droid.unlock.UnLockVaultScreenRoot

class UnLockVaultScreenFactoryImpl: UnLockVaultScreenFactory {

    @Composable
    override fun CreateUnLockVaultScreen() {
        UnLockVaultScreenRoot()
    }
}