package io.github.devapro.data

enum class FileFormat(val displayName: String, val extension: String) {
    CSV("CSV (Comma Separated Values)", "csv"),
    XML("XML (Extensible Markup Language)", "xml"),
    JSON("JSON (JavaScript Object Notation)", "json")
}

fun getFormatDescription(format: FileFormat): String {
    return when (format) {
        FileFormat.CSV -> "Comma-separated values format. Compatible with spreadsheet applications like Excel and Google Sheets. Simple and widely supported."
        FileFormat.XML -> "Extensible Markup Language format. Structured data format that preserves field relationships and is human-readable."
        FileFormat.JSON -> "JavaScript Object Notation format. Lightweight, easy to read, and commonly used for data exchange. Recommended for most use cases."
    }
}