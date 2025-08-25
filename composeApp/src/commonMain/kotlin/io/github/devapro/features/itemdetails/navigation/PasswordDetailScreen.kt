package io.github.devapro.features.itemdetails.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.droid.data.model.ItemModel
import io.github.devapro.features.itemdetails.PasswordDetailScreenRoot

data class PasswordDetailScreen(
    private val item: ItemModel
) : Screen {

    @Composable
    override fun Content() {
        PasswordDetailScreenRoot(item = item)
    }
} 