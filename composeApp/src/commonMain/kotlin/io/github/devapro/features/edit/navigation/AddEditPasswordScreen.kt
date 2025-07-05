package io.github.devapro.features.edit.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.features.edit.AddEditPasswordScreenRoot
import io.github.devapro.model.ItemModel

data class AddEditPasswordScreen(
    private val item: ItemModel? = null
) : Screen {

    @Composable
    override fun Content() {
        AddEditPasswordScreenRoot(item = item)
    }
} 