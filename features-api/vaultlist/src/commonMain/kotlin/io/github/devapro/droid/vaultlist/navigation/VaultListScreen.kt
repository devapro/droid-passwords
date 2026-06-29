package io.github.devapro.droid.vaultlist.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.droid.vaultlist.VaultListScreenFactory
import org.koin.compose.koinInject

object VaultListScreen : Screen {

    @Composable
    override fun Content() {
        val factory: VaultListScreenFactory = koinInject()
        factory.CreateVaultListScreen()
    }
}
