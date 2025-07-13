package io.github.devapro.features.export.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.features.export.ExportScreenRoot

object ExportScreen : Screen {

    @Composable
    override fun Content() {
        ExportScreenRoot()
    }
}