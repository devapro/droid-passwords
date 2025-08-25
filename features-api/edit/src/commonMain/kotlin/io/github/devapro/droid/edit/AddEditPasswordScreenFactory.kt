package io.github.devapro.droid.edit

import androidx.compose.runtime.Composable
import io.github.devapro.droid.data.model.ItemModel

interface AddEditPasswordScreenFactory {

    @Composable
    fun CreateAddEditPasswordScreen(item: ItemModel? = null)
}