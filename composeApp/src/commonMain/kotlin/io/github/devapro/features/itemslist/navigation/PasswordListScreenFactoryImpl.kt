package io.github.devapro.features.itemslist.navigation

import androidx.compose.runtime.Composable
import io.github.devapro.droid.data.vault.VaultItemTag
import io.github.devapro.droid.itemlist.PasswordListScreenFactory
import io.github.devapro.droid.itemlist.PasswordTagFilterType
import io.github.devapro.features.itemslist.PasswordListScreenRoot

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