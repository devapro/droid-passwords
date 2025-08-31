package io.github.devapro.droid.settings.navigation

import androidx.compose.runtime.Composable
import io.github.devapro.droid.settings.SettingsScreenFactory
import io.github.devapro.droid.settings.SettingsScreenRoot

class SettingsScreenFactoryImpl: SettingsScreenFactory {

    @Composable
    override fun CreateSettingsScreen() {
        SettingsScreenRoot()
    }
}