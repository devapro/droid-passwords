package io.github.devapro.droid.data.sync

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSURLCredential
import platform.Foundation.NSURLSessionAuthChallengePerformDefaultHandling
import platform.Foundation.NSURLSessionAuthChallengeUseCredential
import platform.Foundation.create
import platform.Foundation.serverTrust

@OptIn(ExperimentalForeignApi::class)
actual fun createSyncHttpClient(): HttpClient {
    return HttpClient(Darwin) {
        installSyncDefaults()
        engine {
            handleChallenge { _, _, challenge, completionHandler ->
                val serverTrust = challenge.protectionSpace.serverTrust
                if (serverTrust != null) {
                    val credential = NSURLCredential.create(trust = serverTrust)
                    completionHandler(NSURLSessionAuthChallengeUseCredential, credential)
                } else {
                    completionHandler(NSURLSessionAuthChallengePerformDefaultHandling, null)
                }
            }
        }
    }
}
