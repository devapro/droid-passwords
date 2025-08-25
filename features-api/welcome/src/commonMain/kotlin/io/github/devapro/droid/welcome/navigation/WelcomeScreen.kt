package io.github.devapro.droid.welcome.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.droid.welcome.WelcomeScreenFactory
import org.koin.compose.koinInject

object WelcomeScreen: Screen {

    @Composable
    override fun Content() {
        val factory: WelcomeScreenFactory = koinInject()
        factory.CreateWelcomeScreen()
    }
}