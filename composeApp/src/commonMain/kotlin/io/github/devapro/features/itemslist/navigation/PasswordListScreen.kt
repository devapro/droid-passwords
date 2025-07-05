package io.github.devapro.features.itemslist.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.data.vault.VaultItemTag
import io.github.devapro.features.itemslist.PasswordListScreenRoot

data class PasswordListScreen(
    val type: PasswordTagFilterType,
    val tag: VaultItemTag? = null
) : Screen {

    @Composable
    override fun Content() {
        PasswordListScreenRoot(
            type = type,
            tag = tag
        )
    }
} 