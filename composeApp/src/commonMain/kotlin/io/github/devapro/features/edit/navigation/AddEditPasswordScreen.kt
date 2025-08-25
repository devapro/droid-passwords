package io.github.devapro.features.edit.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.droid.data.model.ItemModel
import io.github.devapro.features.edit.AddEditPasswordScreenRoot

data class AddEditPasswordScreen(
    private val item: ItemModel? = null
) : Screen {

    @Composable
    override fun Content() {
        AddEditPasswordScreenRoot(item = item)
    }
} 