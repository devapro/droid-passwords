package io.github.devapro.data

expect class PlatformFileHandler constructor() {
    fun writeTextToFile(fileName: String, content: String)
}