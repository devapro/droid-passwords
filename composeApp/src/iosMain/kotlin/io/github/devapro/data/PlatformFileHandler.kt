package io.github.devapro.data

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSString
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSUserDomainMask
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringByAppendingPathComponent
import platform.Foundation.writeToFile

actual class PlatformFileHandler {

    @OptIn(ExperimentalForeignApi::class)
    actual fun writeTextToFile(fileName: String, content: String) {
        val paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, true)
        val documentsDirectory = paths.first() as NSString
        val filePath = documentsDirectory.stringByAppendingPathComponent(fileName)

        (content as NSString).writeToFile(filePath, atomically = true, encoding = NSUTF8StringEncoding, error = null)
    }
}