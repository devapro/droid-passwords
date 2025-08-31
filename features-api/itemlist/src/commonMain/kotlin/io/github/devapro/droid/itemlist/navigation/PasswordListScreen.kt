package io.github.devapro.droid.itemlist.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.droid.data.vault.VaultItemTag
import io.github.devapro.droid.itemlist.PasswordListScreenFactory
import io.github.devapro.droid.itemlist.PasswordTagFilterType
import org.koin.compose.koinInject

data class PasswordListScreen(
    val type: PasswordTagFilterType,
    val tag: VaultItemTag? = null
) : Screen {

    @Composable
    override fun Content() {
        val factory: PasswordListScreenFactory = koinInject()
        factory.CreatePasswordListScreen(
            type = type,
            tag = tag
        )
    }
}