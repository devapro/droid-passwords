package io.github.devapro.droid.sync

import io.ktor.network.tls.certificates.buildKeyStore
import io.ktor.network.tls.certificates.saveToFile
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json
import java.io.File

/**
 * Entry point. We make sure a self-signed keystore exists (generating one on the
 * fly if needed) and then hand off to Ktor's [EngineMain], which reads the HTTP
 * and HTTPS connector configuration from `application.conf`.
 */
fun main(args: Array<String>) {
    ensureKeyStore()
    EngineMain.main(args)
}

private fun ensureKeyStore() {
    val path = System.getenv("KEYSTORE_PATH") ?: "certs/keystore.jks"
    val alias = System.getenv("KEY_ALIAS") ?: "droidpasswords"
    val keyStorePassword = System.getenv("KEYSTORE_PASSWORD") ?: "changeit"
    val keyPassword = System.getenv("KEY_PASSWORD") ?: "changeit"

    val file = File(path)
    if (file.exists()) return

    file.parentFile?.mkdirs()
    val extraDomains = (System.getenv("CERT_DOMAINS") ?: "localhost")
        .split(",")
        .map { it.trim() }
        .filter { it.isNotEmpty() }

    val keyStore = buildKeyStore {
        certificate(alias) {
            password = keyPassword
            domains = extraDomains
        }
    }
    keyStore.saveToFile(file, keyStorePassword)
    println("Generated self-signed keystore at ${file.absolutePath} for domains: $extraDomains")
}

private val storage: Storage by lazy {
    Storage(System.getenv("DB_PATH") ?: "data/sync.db")
}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    install(CallLogging)
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(cause.message ?: "Internal server error")
            )
        }
    }

    routing {
        get("/health") {
            call.respond(mapOf("status" to "ok"))
        }

        post("/auth/register") {
            val request = call.receive<RegisterRequest>()
            if (request.username.isBlank() || request.password.length < 4) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse("Username required and password must be at least 4 characters")
                )
                return@post
            }
            val salt = Crypto.generateSalt()
            val hash = Crypto.hashPassword(request.password, salt)
            val user = storage.createUser(request.username, salt, hash)
            if (user == null) {
                call.respond(HttpStatusCode.Conflict, ErrorResponse("Username already exists"))
                return@post
            }
            val token = Crypto.generateToken()
            storage.createToken(user.id, token)
            call.respond(AuthResponse(token = token, username = user.username))
        }

        post("/auth/login") {
            val request = call.receive<LoginRequest>()
            val user = storage.findUser(request.username)
            if (user == null || !Crypto.verifyPassword(request.password, user.salt, user.passwordHash)) {
                call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Invalid credentials"))
                return@post
            }
            val token = Crypto.generateToken()
            storage.createToken(user.id, token)
            call.respond(AuthResponse(token = token, username = user.username))
        }

        get("/sync/vaults") {
            val userId = call.authenticate() ?: return@get
            call.respond(VaultListResponse(vaults = storage.listVaults(userId)))
        }

        get("/sync/{vaultId}/changes") {
            val userId = call.authenticate() ?: return@get
            val vaultId = call.vaultId() ?: return@get
            val since = call.request.queryParameters["since"]?.toLongOrNull() ?: 0L
            val limit = (call.request.queryParameters["limit"]?.toIntOrNull() ?: 500).coerceIn(1, 1000)
            val items = storage.changesSince(userId, vaultId, since, limit)
            val latest = storage.latestSeq(userId, vaultId)
            call.respond(ChangesResponse(items = items, latestSeq = latest))
        }

        post("/sync/{vaultId}/push") {
            val userId = call.authenticate() ?: return@post
            val vaultId = call.vaultId() ?: return@post
            val request = call.receive<PushRequest>()
            val results = request.items.map { storage.applyPush(userId, vaultId, it) }
            call.respond(PushResponse(results = results, latestSeq = storage.latestSeq(userId, vaultId)))
        }

        post("/sync/{vaultId}/meta") {
            val userId = call.authenticate() ?: return@post
            val vaultId = call.vaultId() ?: return@post
            val request = call.receive<VaultMetaRequest>()
            storage.setVaultMeta(
                userId = userId,
                vaultId = vaultId,
                name = request.name,
                nameUpdatedAt = request.nameUpdatedAt,
                deleted = request.deleted,
                updatedAt = request.updatedAt
            )
            call.respond(HttpStatusCode.OK, mapOf("status" to "ok"))
        }
    }
}

/**
 * Resolves the bearer token to a user id, or responds 401 and returns null.
 */
private suspend fun io.ktor.server.application.ApplicationCall.authenticate(): Long? {
    val header = request.headers["Authorization"]
    val token = header?.removePrefix("Bearer ")?.trim()
    if (token.isNullOrEmpty()) {
        respond(HttpStatusCode.Unauthorized, ErrorResponse("Missing token"))
        return null
    }
    val userId = storage.userIdForToken(token)
    if (userId == null) {
        respond(HttpStatusCode.Unauthorized, ErrorResponse("Invalid token"))
        return null
    }
    return userId
}

/**
 * Extracts the `{vaultId}` path segment, or responds 400 and returns null.
 */
private suspend fun io.ktor.server.application.ApplicationCall.vaultId(): String? {
    val vaultId = parameters["vaultId"]?.trim()
    if (vaultId.isNullOrEmpty()) {
        respond(HttpStatusCode.BadRequest, ErrorResponse("Missing vault id"))
        return null
    }
    return vaultId
}
