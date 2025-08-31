package io.github.devapro.features.itemdetails.navigation

import androidx.compose.runtime.Composable
import io.github.devapro.droid.data.model.ItemModel
import io.github.devapro.droid.itemdetails.PasswordDetailScreenFactory
import io.github.devapro.features.itemdetails.PasswordDetailScreenRoot

class PasswordDetailScreenFactoryImpl: PasswordDetailScreenFactory {

    @Composable
    override fun CreatePasswordDetailScreen(item: ItemModel) {
        PasswordDetailScreenRoot(item = item)
    }
}