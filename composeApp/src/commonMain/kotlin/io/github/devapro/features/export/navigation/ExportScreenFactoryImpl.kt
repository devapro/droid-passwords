package io.github.devapro.features.export.navigation

import androidx.compose.runtime.Composable
import io.github.devapro.droid.export.ExportScreenFactory
import io.github.devapro.features.export.ExportScreenRoot

class ExportScreenFactoryImpl: ExportScreenFactory {

    @Composable
    override fun CreateExportScreen() {
        ExportScreenRoot()
    }
}