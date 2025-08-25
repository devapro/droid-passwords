package io.github.devapro.droid.data

enum class FileFormat(val displayName: String, val extension: String) {
    CSV("CSV (Comma Separated Values)", "csv"),
    XML("XML (Extensible Markup Language)", "xml"),
    JSON("JSON (JavaScript Object Notation)", "json")
}