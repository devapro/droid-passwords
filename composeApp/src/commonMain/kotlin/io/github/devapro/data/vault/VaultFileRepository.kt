package io.github.devapro.data.vault

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.cacheDir
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.readString
import io.github.vinceglb.filekit.writeString
import kotlinx.serialization.json.Json

private const val DEFAULT_FILE_NAME = "droid-cache.json"

class VaultFileRepository(
    private val json: Json
) {

    fun isVaultExists(): Boolean {
        val file = getVaultFile()
        return file.exists()
    }

    suspend fun createVault(password: String): Boolean { // TODO replace with AppResult

        println("Creating vault at: ${FileKit.cacheDir.absolutePath()}")

        val file = getVaultFile()
        val vaultModel = VaultModel(
            items = emptyList()
        )
        val vaultRawContent = json.encodeToString(vaultModel)

        // TODO add encryption logic here using the provided password
        file.writeString(vaultRawContent)

        return true
    }

    fun changePassword(oldPassword: String, newPassword: String): Boolean {
        // Logic to change the vault password from oldPassword to newPassword
        return true // Return true if successful, false otherwise
    }

    suspend fun getVault(password: String): VaultModel {
        val file = getVaultFile()
        if (!file.exists()) {
            return VaultModel(items = emptyList())
        }
        val vaultRawContent = file.readString()
        return json.decodeFromString(vaultRawContent)
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

