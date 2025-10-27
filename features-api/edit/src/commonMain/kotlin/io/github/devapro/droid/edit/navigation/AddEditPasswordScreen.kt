package io.github.devapro.droid.edit.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.droid.data.model.ItemModel
import io.github.devapro.droid.data.vault.VaultItemTag
import io.github.devapro.droid.edit.AddEditPasswordScreenFactory
import org.koin.compose.koinInject

data class AddEditPasswordScreen(
    private val item: ItemModel? = null,
    private val selectedTag: VaultItemTag? = null
) : Screen {

    @Composable
    override fun Content() {
        val factory: AddEditPasswordScreenFactory = koinInject()
        factory.CreateAddEditPasswordScreen(
            item = item,
            selectedTag = selectedTag
        )
    }
} 