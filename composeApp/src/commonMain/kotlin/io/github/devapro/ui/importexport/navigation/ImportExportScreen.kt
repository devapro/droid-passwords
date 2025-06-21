package io.github.devapro.ui.importexport.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.ui.importexport.ImportExportScreenRoot

object ImportExportScreen: Screen {

    @Composable
    override fun Content() {
        ImportExportScreenRoot()
    }
}