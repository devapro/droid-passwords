package io.github.devapro.features.itemslist.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.features.itemslist.PasswordListScreenRoot

object PasswordListScreen : Screen {

    @Composable
    override fun Content() {
        PasswordListScreenRoot()
    }
} 