package io.github.devapro.data.vault

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.filesDir
import io.github.vinceglb.filekit.readString
import io.github.vinceglb.filekit.writeString
import kotlinx.serialization.json.Json

private const val DEFAULT_FILE_NAME = "droidpass.json"

class VaultFileRepository(
    private val json: Json
) {

    fun isVaultExists(): Boolean {
        val file = PlatformFile(FileKit.filesDir, DEFAULT_FILE_NAME)
        return file.exists()
    }

    suspend fun createVault(password: String): Boolean { // TODO replace with AppResult
        val file = PlatformFile(FileKit.filesDir, DEFAULT_FILE_NAME)
        val vaultModel = VaultModel(
            items = emptyList()
        )
        val vaultRawContent = json.encodeToString(vaultModel)
        // TODO add encryption logic here using the provided password
        file.writeString(vaultRawContent)
        return true // Return true if successful, false otherwise
    }

    fun changePassword(oldPassword: String, newPassword: String): Boolean {
        // Logic to change the vault password from oldPassword to newPassword
        return true // Return true if successful, false otherwise
    }

    suspend fun getVault(password: String): VaultModel {
        val file = PlatformFile(FileKit.filesDir, DEFAULT_FILE_NAME)
        if (!file.exists()) {
            return VaultModel(items = emptyList())
        }
        val vaultRawContent = file.readString()
        return json.decodeFromString(vaultRawContent)
    }

    suspend fun saveVault(vaultModel: VaultModel, password: String): Boolean {
        val file = PlatformFile(FileKit.filesDir, DEFAULT_FILE_NAME)
        val vaultRawContent = json.encodeToString(vaultModel)
        // TODO add encryption logic here if needed
        file.writeString(vaultRawContent)
        return true // Return true if successful, false otherwise
    }
}

