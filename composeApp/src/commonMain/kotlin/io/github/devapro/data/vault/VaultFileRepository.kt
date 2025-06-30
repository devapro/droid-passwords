package io.github.devapro.data.vault

import io.github.devapro.core.mvi.AppResult
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.cacheDir
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.readBytes
import io.github.vinceglb.filekit.write
import io.github.vinceglb.filekit.writeString
import kotlinx.serialization.json.Json

private const val DEFAULT_FILE_NAME = "droid-d3.data"

class VaultFileRepository(
    private val json: Json,
    private val cryptoMapper: CryptoMapper
) {

    fun isVaultExists(): Boolean {
        val file = getVaultFile()
        return file.exists()
    }

    suspend fun createVault(password: String): AppResult<Unit> {
        try {
            val file = getVaultFile()
            val vaultModel = VaultModel(
                items = emptyList()
            )
            val vaultRawContent = json.encodeToString(vaultModel)

            val vaultEncodedContent = cryptoMapper.encode(password, vaultRawContent)

            file.write(vaultEncodedContent)
        } catch (e: Exception) {
            return AppResult.Failure(e)
        }

        return AppResult.Success(Unit)
    }

    suspend fun changePassword(oldPassword: String, newPassword: String): AppResult<Unit> {
        // Logic to change the vault password from oldPassword to newPassword
        return try {
            val file = getVaultFile()
            if (!file.exists()) {
                return AppResult.Failure(Exception("Vault does not exist"))
            }

            val vaultEncodedContent = file.readBytes()
            val vaultRawContent = cryptoMapper.decode(oldPassword, vaultEncodedContent)
            val vaultModel = json.decodeFromString<VaultModel>(vaultRawContent)

            // Re-encode with the new password
            val newVaultRawContent = json.encodeToString(vaultModel)
            val newVaultEncodedContent = cryptoMapper.encode(newPassword, newVaultRawContent)

            file.write(newVaultEncodedContent)
            return AppResult.Success(Unit)
        } catch (e: Exception) {
            return AppResult.Failure(e)
        }
    }

    suspend fun getVault(password: String): AppResult<VaultModel> {
        val file = getVaultFile()
        if (!file.exists()) {
            return AppResult.Failure(Exception("Vault does not exist"))
        }
        val vaultEncodedContent = file.readBytes()
        val vaultRawContent = cryptoMapper.decode(password, vaultEncodedContent)
        return AppResult.Success(json.decodeFromString(vaultRawContent))
    }

    suspend fun saveVault(vaultModel: VaultModel, password: String): Boolean {
        val file = getVaultFile()
        val vaultRawContent = json.encodeToString(vaultModel)
        // TODO add encryption logic here if needed
        file.writeString(vaultRawContent)
        return true // Return true if successful, false otherwise
    }

    private fun getVaultFile(): PlatformFile {
        return PlatformFile(FileKit.cacheDir, DEFAULT_FILE_NAME)
    }
}

