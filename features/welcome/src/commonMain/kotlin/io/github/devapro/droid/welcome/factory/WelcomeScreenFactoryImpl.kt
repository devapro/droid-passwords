package io.github.devapro.droid.welcome.factory

import androidx.compose.runtime.Composable
import io.github.devapro.droid.welcome.WelcomeScreenFactory
import io.github.devapro.droid.welcome.WelcomeScreenRoot

class WelcomeScreenFactoryImpl: WelcomeScreenFactory {

    @Composable
    override fun CreateWelcomeScreen() {
        WelcomeScreenRoot()
    }
}