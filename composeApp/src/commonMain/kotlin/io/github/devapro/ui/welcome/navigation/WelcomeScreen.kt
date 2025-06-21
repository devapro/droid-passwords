package io.github.devapro.ui.welcome.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.ui.welcome.WelcomeScreenRoot

object WelcomeScreen: Screen {

    @Composable
    override fun Content() {
        WelcomeScreenRoot()
    }
}