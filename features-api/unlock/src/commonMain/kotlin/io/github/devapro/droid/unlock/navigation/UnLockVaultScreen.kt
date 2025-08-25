package io.github.devapro.droid.unlock.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.droid.unlock.UnLockVaultScreenFactory
import org.koin.compose.koinInject

object UnLockVaultScreen : Screen {

    @Composable
    override fun Content() {
        val factory: UnLockVaultScreenFactory = koinInject()
        factory.CreateUnLockVaultScreen()
    }
} 