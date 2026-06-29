package io.github.devapro.droid.data.sync

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.data.sync.model.AuthResponse
import io.github.devapro.droid.data.sync.model.ChangesResponse
import io.github.devapro.droid.data.sync.model.LoginRequest
import io.github.devapro.droid.data.sync.model.PushItem
import io.github.devapro.droid.data.sync.model.PushRequest
import io.github.devapro.droid.data.sync.model.PushResponse
import io.github.devapro.droid.data.sync.model.RegisterRequest
import io.github.devapro.droid.data.sync.model.VaultListResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess

/**
 * Thin HTTP wrapper around the sync server endpoints. The [HttpClient] is shared
 * (and lazily created) so the underlying engine/connection pool is reused.
 */
class SyncApi(
    private val clientProvider: () -> HttpClient
) {

    private val client: HttpClient by lazy { clientProvider() }

    suspend fun register(baseUrl: String, username: String, password: String): AppResult<AuthResponse> =
        runCatchingResult {
            val response = client.post("${normalize(baseUrl)}/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(RegisterRequest(username, password))
            }
            if (!response.status.isSuccess()) {
                error(describe(response.status, "Registration failed"))
            }
            response.body<AuthResponse>()
        }

    suspend fun login(baseUrl: String, username: String, password: String): AppResult<AuthResponse> =
        runCatchingResult {
            val response = client.post("${normalize(baseUrl)}/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(username, password))
            }
            if (!response.status.isSuccess()) {
                error(describe(response.status, "Login failed"))
            }
            response.body<AuthResponse>()
        }

    suspend fun listVaults(
        baseUrl: String,
        token: String
    ): AppResult<VaultListResponse> = runCatchingResult {
        val response = client.get("${normalize(baseUrl)}/sync/vaults") {
            bearerAuth(token)
        }
        if (!response.status.isSuccess()) {
            error(describe(response.status, "Failed to list vaults"))
        }
        response.body<VaultListResponse>()
    }

    suspend fun getChanges(
        baseUrl: String,
        token: String,
        vaultId: String,
        since: Long,
        limit: Int = 500
    ): AppResult<ChangesResponse> = runCatchingResult {
        val response = client.get("${normalize(baseUrl)}/sync/$vaultId/changes") {
            bearerAuth(token)
            parameter("since", since)
            parameter("limit", limit)
        }
        if (!response.status.isSuccess()) {
            error(describe(response.status, "Failed to fetch changes"))
        }
        response.body<ChangesResponse>()
    }

    suspend fun push(
        baseUrl: String,
        token: String,
        vaultId: String,
        items: List<PushItem>
    ): AppResult<PushResponse> = runCatchingResult {
        val response = client.post("${normalize(baseUrl)}/sync/$vaultId/push") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(PushRequest(items))
        }
        if (!response.status.isSuccess()) {
            error(describe(response.status, "Failed to push changes"))
        }
        response.body<PushResponse>()
    }

    private fun describe(status: HttpStatusCode, fallback: String): String = when (status) {
        HttpStatusCode.Unauthorized -> "Not authorized. Please log in again."
        HttpStatusCode.Conflict -> "Username already exists."
        else -> "$fallback (${status.value})"
    }

    private fun normalize(baseUrl: String): String = baseUrl.trim().trimEnd('/')

    private inline fun <T> runCatchingResult(block: () -> T): AppResult<T> = try {
        AppResult.Success(block())
    } catch (e: Exception) {
        AppResult.Failure(e)
    }
}
