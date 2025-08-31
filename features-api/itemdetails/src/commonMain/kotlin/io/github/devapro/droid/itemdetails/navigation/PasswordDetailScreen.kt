package io.github.devapro.droid.itemdetails.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.droid.data.model.ItemModel
import io.github.devapro.droid.itemdetails.PasswordDetailScreenFactory
import org.koin.compose.koinInject

data class PasswordDetailScreen(
    private val item: ItemModel
) : Screen {

    @Composable
    override fun Content() {
        val factory: PasswordDetailScreenFactory = koinInject()
        factory.CreatePasswordDetailScreen(item = item)
    }
}