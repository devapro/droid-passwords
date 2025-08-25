package io.github.devapro.droid.importdata.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.droid.importdata.ImportScreenFactory
import org.koin.compose.koinInject

object ImportScreen : Screen {

    @Composable
    override fun Content() {
        val factory: ImportScreenFactory = koinInject()
        factory.CreateImportScreen()
    }
}