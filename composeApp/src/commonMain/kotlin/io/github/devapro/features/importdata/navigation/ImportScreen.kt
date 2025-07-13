package io.github.devapro.features.importdata.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.features.importdata.ImportScreenRoot

object ImportScreen : Screen {

    @Composable
    override fun Content() {
        ImportScreenRoot()
    }
}