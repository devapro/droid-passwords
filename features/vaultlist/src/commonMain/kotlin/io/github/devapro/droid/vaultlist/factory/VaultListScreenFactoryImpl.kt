package io.github.devapro.droid.vaultlist.factory

import androidx.compose.runtime.Composable
import io.github.devapro.droid.vaultlist.VaultListScreenFactory
import io.github.devapro.droid.vaultlist.VaultListScreenRoot

class VaultListScreenFactoryImpl : VaultListScreenFactory {

    @Composable
    override fun CreateVaultListScreen() {
        VaultListScreenRoot()
    }
}
