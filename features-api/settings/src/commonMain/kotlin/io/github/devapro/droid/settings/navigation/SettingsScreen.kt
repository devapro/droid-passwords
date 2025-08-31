package io.github.devapro.droid.settings.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.droid.settings.SettingsScreenFactory
import org.koin.compose.koinInject

data object SettingsScreen : Screen {

    @Composable
    override fun Content() {
        val factory: SettingsScreenFactory = koinInject()
        factory.CreateSettingsScreen()
    }
}