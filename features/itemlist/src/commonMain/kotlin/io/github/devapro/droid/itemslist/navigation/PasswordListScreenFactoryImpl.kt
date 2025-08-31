package io.github.devapro.droid.itemslist.navigation

import androidx.compose.runtime.Composable
import io.github.devapro.droid.data.vault.VaultItemTag
import io.github.devapro.droid.itemlist.PasswordListScreenFactory
import io.github.devapro.droid.itemlist.PasswordTagFilterType
import io.github.devapro.droid.itemslist.PasswordListScreenRoot

class PasswordListScreenFactoryImpl: PasswordListScreenFactory {

    @Composable
    override fun CreatePasswordListScreen(
        type: PasswordTagFilterType,
        tag: VaultItemTag?
    ) {
        PasswordListScreenRoot(
            type = type,
            tag = tag
        )
    }
}