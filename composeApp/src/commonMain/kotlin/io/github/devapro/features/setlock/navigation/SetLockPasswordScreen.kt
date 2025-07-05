package io.github.devapro.features.setlock.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.features.setlock.SetLockPasswordScreenRoot

object SetLockPasswordScreen: Screen {

    @Composable
    override fun Content() {
        SetLockPasswordScreenRoot()
    }
}