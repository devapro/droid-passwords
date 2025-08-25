package io.github.devapro.droid.setlock.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.droid.setlock.SetLockPasswordScreenFactory
import org.koin.compose.koinInject

object SetLockPasswordScreen: Screen {

    @Composable
    override fun Content() {
        val factory: SetLockPasswordScreenFactory = koinInject()
        factory.CreateSetLockPasswordScreen()
    }
}