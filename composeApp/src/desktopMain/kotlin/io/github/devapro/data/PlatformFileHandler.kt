package io.github.devapro.data

import net.harawata.appdirs.AppDirsFactory
import java.io.File

const val PACKAGE_NAME = "io.github.devapro"
const val VERSION = "1.0"
const val ORGANISATION = "devapro"

actual class PlatformFileHandler {

    val filesDir = AppDirsFactory.getInstance().getUserDataDir(PACKAGE_NAME, VERSION, ORGANISATION)
    val cacheDir = AppDirsFactory.getInstance().getUserCacheDir(PACKAGE_NAME, VERSION, ORGANISATION)

    actual fun writeTextToFile(fileName: String, content: String) {
        println(cacheDir)
        val file = File(cacheDir + "/" + fileName) // Or specify a path
        file.writeText(content)
        println(file.absolutePath)
    }
}