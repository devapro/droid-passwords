package io.github.devapro.ui.unlock.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.ui.unlock.UnLockVaultScreenRoot

object UnLockVaultScreen : Screen {

    @Composable
    override fun Content() {
        UnLockVaultScreenRoot()
    }
} 