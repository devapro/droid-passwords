package io.github.devapro.features.unlock.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.features.unlock.UnLockVaultScreenRoot

object UnLockVaultScreen : Screen {

    @Composable
    override fun Content() {
        UnLockVaultScreenRoot()
    }
} 