package io.github.devapro.droid.itemdetails

import androidx.compose.runtime.Composable
import io.github.devapro.droid.data.model.ItemModel

interface PasswordDetailScreenFactory {

    @Composable
    fun CreatePasswordDetailScreen(item: ItemModel)
}