package io.github.devapro.droid.importdata.factory

import io.github.devapro.droid.importdata.model.FileFormat
import io.github.devapro.droid.importdata.model.FormatModel

class FormatsListFactory {

    fun createFormatsList(
        selectedFormat: String? = null
    ): List<FormatModel> {
        return listOf(
            FormatModel(
                name = "CSV",
                format = FileFormat.CSV,
                description = "Comma-separated values format",
                shortDescription = "CSV",
                isSelected = selectedFormat == "CSV"
            ),
            FormatModel(
                name = "JSON",
                format = FileFormat.JSON,
                description = "JavaScript Object Notation format",
                shortDescription = "JSON",
                isSelected = selectedFormat == "JSON"
            ),
            FormatModel(
                name = "DATA",
                format = FileFormat.DATA,
                description = "Custom data format",
                shortDescription = "DATA",
                isSelected = selectedFormat == "DATA"
            )
        )
    }
}