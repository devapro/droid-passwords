package io.github.devapro.droid.edit.factory

import androidx.compose.runtime.Composable
import io.github.devapro.droid.data.model.ItemModel
import io.github.devapro.droid.edit.AddEditPasswordScreenFactory
import io.github.devapro.droid.edit.AddEditPasswordScreenRoot

class AddEditPasswordScreenFactoryImpl: AddEditPasswordScreenFactory {

    @Composable
    override fun CreateAddEditPasswordScreen(item: ItemModel?) {
        AddEditPasswordScreenRoot(item = item)
    }
}