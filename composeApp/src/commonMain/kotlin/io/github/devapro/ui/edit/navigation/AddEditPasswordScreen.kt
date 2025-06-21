package io.github.devapro.ui.edit.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.model.ItemModel
import io.github.devapro.ui.edit.AddEditPasswordScreenRoot

data class AddEditPasswordScreen(
    private val item: ItemModel? = null
) : Screen {

    @Composable
    override fun Content() {
        AddEditPasswordScreenRoot(item = item)
    }
} 