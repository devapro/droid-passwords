package io.github.devapro.features.settings.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.features.settings.SettingsScreenRoot

data object SettingsScreen : Screen {

    @Composable
    override fun Content() {
        SettingsScreenRoot()
    }
}