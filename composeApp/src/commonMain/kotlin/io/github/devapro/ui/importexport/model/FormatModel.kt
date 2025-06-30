package io.github.devapro.ui.importexport.model

data class FormatModel(
    val format: FileFormat,
    val name: String,
    val description: String,
    val shortDescription: String,
    val isSelected: Boolean = false
)
