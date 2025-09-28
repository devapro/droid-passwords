package io.github.devapro.droid.itemlist

import androidx.compose.runtime.Composable
import io.github.devapro.droid.data.vault.VaultItemTag

interface PasswordListScreenFactory {

    @Composable
    fun CreatePasswordListScreen(
        type: PasswordTagFilterType,
        tag: VaultItemTag?
    )
}