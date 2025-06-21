package io.github.devapro.ui.setlock.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.ui.setlock.SetLockPasswordScreenRoot

object SetLockPasswordScreen: Screen {

    @Composable
    override fun Content() {
        SetLockPasswordScreenRoot()
    }
}