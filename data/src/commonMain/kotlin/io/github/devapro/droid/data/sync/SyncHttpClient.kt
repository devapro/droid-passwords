package io.github.devapro.droid.data.sync

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Creates a platform HTTP client configured to accept the self-signed
 * certificate of a self-hosted sync server.
 *
 * Security note: the client trusts the server certificate without verifying a
 * public CA chain, which is the expected setup for a self-hosted, self-signed
 * server. Account credentials are still protected by TLS in transit, and item
 * payloads are end-to-end encrypted before they ever leave the device.
 */
expect fun createSyncHttpClient(): HttpClient

internal fun HttpClientConfig<*>.installSyncDefaults() {
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
            }
        )
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 30_000
        connectTimeoutMillis = 15_000
        socketTimeoutMillis = 30_000
    }
    install(Logging) {
        level = LogLevel.INFO
        // Never let the bearer token reach the logs, even if the level is raised to HEADERS/ALL.
        sanitizeHeader { header -> header == HttpHeaders.Authorization }
    }
}
