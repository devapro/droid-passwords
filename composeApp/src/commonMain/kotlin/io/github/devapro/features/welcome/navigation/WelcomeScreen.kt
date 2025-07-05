package io.github.devapro.features.welcome.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.features.welcome.WelcomeScreenRoot

object WelcomeScreen: Screen {

    @Composable
    override fun Content() {
        WelcomeScreenRoot()
    }
}