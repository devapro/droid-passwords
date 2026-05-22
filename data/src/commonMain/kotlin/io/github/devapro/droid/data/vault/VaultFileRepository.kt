package io.github.devapro.droid.data.vault

import io.github.devapro.droid.core.mvi.AppResult
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.cacheDir
import io.github.vinceglb.filekit.delete
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.filesDir
import io.github.vinceglb.filekit.readBytes
import io.github.vinceglb.filekit.write
import kotlinx.serialization.json.Json

private const val LEGACY_FILE_NAME = "droid-d4.data"

class VaultFileRepository(
    private val json: Json,
    private val cryptoMapper: CryptoMapper,
) {

    fun exists(descriptor: VaultDescriptor): Boolean = resolveFile(descriptor).exists()

    fun resolveFile(descriptor: VaultDescriptor): PlatformFile =
        PlatformFile(FileKit.filesDir, descriptor.fileName)

    suspend fun createVault(
        descriptor: VaultDescriptor,
        password: String,
    ): AppResult<VaultModel> {
        return try {
            val now = descriptor.createdAt
            val vaultModel = VaultModel(
                password = password,
                items = emptyList(),
                name = descriptor.name,
                createdAt = now,
                updatedAt = now,
            )
            val raw = json.encodeToString(vaultModel)
            val encoded = cryptoMapper.encode(password, raw)
            resolveFile(descriptor).write(encoded)
            AppResult.Success(vaultModel)
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    suspend fun changePassword(
        descriptor: VaultDescriptor,
        oldPassword: String,
        newPassword: String,
    ): AppResult<VaultModel> {
        return try {
            val file = resolveFile(descriptor)
            if (!file.exists()) {
                return AppResult.Failure(Exception("Vault does not exist"))
            }
            val encoded = file.readBytes()
            val raw = cryptoMapper.decode(oldPassword, encoded)
            val existing = json.decodeFromString<VaultModel>(raw)
            val updated = existing.copy(
                password = newPassword,
                updatedAt = currentTimeMillis(),
            )
            val newRaw = json.encodeToString(updated)
            val newEncoded = cryptoMapper.encode(newPassword, newRaw)
            file.write(newEncoded)
            AppResult.Success(updated)
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    suspend fun getVault(
        descriptor: VaultDescriptor,
        password: String,
    ): AppResult<VaultModel> {
        return try {
            val file = resolveFile(descriptor)
            if (!file.exists()) {
                return AppResult.Failure(Exception("Vault does not exist"))
            }
            val encoded = file.readBytes()
            val raw = cryptoMapper.decode(password, encoded)
            AppResult.Success(json.decodeFromString(raw))
        } catch (_: Exception) {
            AppResult.Failure(Exception("Failed to load vault. Please check your password or file."))
        }
    }

    suspend fun getVaultFromExternalFile(
        file: PlatformFile,
        password: String,
    ): AppResult<VaultModel> {
        return try {
            if (!file.exists()) {
                return AppResult.Failure(Exception("File does not exist"))
            }
            val encoded = file.readBytes()
            val raw = cryptoMapper.decode(password, encoded)
            AppResult.Success(json.decodeFromString(raw))
        } catch (_: Exception) {
            AppResult.Failure(Exception("Failed to load vault. Please check your password or file."))
        }
    }

    suspend fun saveVault(
        descriptor: VaultDescriptor,
        vaultModel: VaultModel,
    ): AppResult<Unit> {
        return try {
            val raw = json.encodeToString(vaultModel)
            val encoded = cryptoMapper.encode(vaultModel.password, raw)
            resolveFile(descriptor).write(encoded)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    suspend fun saveVaultToExternalFile(
        vaultModel: VaultModel,
        file: PlatformFile,
    ): AppResult<Unit> {
        return try {
            val raw = json.encodeToString(vaultModel)
            val encoded = cryptoMapper.encode(vaultModel.password, raw)
            file.write(encoded)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    suspend fun deleteVaultFile(descriptor: VaultDescriptor): AppResult<Unit> {
        return try {
            val file = resolveFile(descriptor)
            if (file.exists()) file.delete()
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    fun legacyVaultExists(): Boolean = legacyVaultFile().exists()

    /**
     * Moves the legacy `droid-d4.data` from cacheDir into the durable filesDir under the
     * descriptor's file name. Encrypted bytes are preserved as-is — the file is never decrypted,
     * so the user's existing master password keeps working.
     */
    suspend fun migrateLegacyVault(descriptor: VaultDescriptor): AppResult<Unit> {
        return try {
            val legacy = legacyVaultFile()
            if (!legacy.exists()) {
                return AppResult.Failure(Exception("Legacy vault does not exist"))
            }
            val bytes = legacy.readBytes()
            resolveFile(descriptor).write(bytes)
            legacy.delete()
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    private fun legacyVaultFile(): PlatformFile =
        PlatformFile(FileKit.cacheDir, LEGACY_FILE_NAME)

    private fun currentTimeMillis(): Long {
        @OptIn(kotlin.time.ExperimentalTime::class)
        return kotlin.time.Clock.System.now().toEpochMilliseconds()
    }
}
