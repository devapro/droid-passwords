package io.github.devapro.features.importexport.model

enum class FileFormat(val fileExtension: String) {
    CSV("csv"),
    XML("xml"),
    JSON("json"),
    DATA("data");
}