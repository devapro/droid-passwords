package io.github.devapro.droid.importdata.factory

import androidx.compose.runtime.Composable
import io.github.devapro.droid.importdata.ImportScreenFactory
import io.github.devapro.droid.importdata.ImportScreenRoot

class ImportScreenFactoryImpl: ImportScreenFactory {

    @Composable
    override fun CreateImportScreen() {
        ImportScreenRoot()
    }
}