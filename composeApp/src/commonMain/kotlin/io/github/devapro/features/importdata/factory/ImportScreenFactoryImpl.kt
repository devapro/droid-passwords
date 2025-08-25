package io.github.devapro.features.importdata.factory

import androidx.compose.runtime.Composable
import io.github.devapro.droid.importdata.ImportScreenFactory
import io.github.devapro.features.importdata.ImportScreenRoot

class ImportScreenFactoryImpl: ImportScreenFactory {

    @Composable
    override fun CreateImportScreen() {
        ImportScreenRoot()
    }
}