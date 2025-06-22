package io.github.devapro.data

import io.github.devapro.DroidPasswordApplication
import java.io.File

actual class PlatformFileHandler {
    actual fun writeTextToFile(fileName: String, content: String) {
        println("Writing to file: $fileName")
        val file = File(DroidPasswordApplication.instance.getApplicationContext().cacheDir, fileName) // Or other suitable directory
        file.writeText(content)
        println("File written to: ${file.absolutePath}")
    }
}