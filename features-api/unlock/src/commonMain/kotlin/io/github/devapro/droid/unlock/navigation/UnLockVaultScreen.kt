package io.github.devapro.droid.unlock.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.droid.unlock.UnLockVaultScreenFactory
import org.koin.compose.koinInject

data class UnLockVaultScreen(val vaultId: String? = null) : Screen {

    @Composable
    override fun Content() {
        val factory: UnLockVaultScreenFactory = koinInject()
        factory.CreateUnLockVaultScreen(vaultId)
    }
}
