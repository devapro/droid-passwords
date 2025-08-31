package io.github.devapro.droid.export.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.droid.export.ExportScreenFactory
import org.koin.compose.koinInject

object ExportScreen : Screen {

    @Composable
    override fun Content() {
        val factory: ExportScreenFactory = koinInject()
        factory.CreateExportScreen()
    }
}