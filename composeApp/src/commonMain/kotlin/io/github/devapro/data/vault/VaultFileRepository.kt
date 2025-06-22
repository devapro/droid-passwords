package io.github.devapro.data.vault

import io.github.devapro.data.PlatformFileHandler
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.cacheDir
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.filesDir
import io.github.vinceglb.filekit.list
import io.github.vinceglb.filekit.path
import io.github.vinceglb.filekit.readString
import io.github.vinceglb.filekit.write
import io.github.vinceglb.filekit.writeString
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import kotlinx.serialization.json.Json

private const val DEFAULT_FILE_NAME = "1v.json"

class VaultFileRepository(
    private val json: Json
) {

    suspend fun isVaultExists(): Boolean {
        val file = PlatformFile(FileKit.cacheDir, DEFAULT_FILE_NAME)
        println("Checking if vault exists at:")
        println(file.absolutePath())
        println(file.path.toString())
        return file.exists()
//        val path = file.absolutePath()
//        val store: KStore<VaultModel> = storeOf(
//            file = Path(path),
//            json = json
//        )
//        return store.get() != null.also {
//            store.close()
//        }
    }

    suspend fun createVault(password: String): Boolean { // TODO replace with AppResult

        println("Creating vault at: ${FileKit.cacheDir.absolutePath()}")
//        PlatformFileHandler().writeTextToFile(
//            fileName = DEFAULT_FILE_NAME,
//            content = "TTTTTTTTTTTTTTTTTTTTTT" // Placeholder content, replace with actual vault creation logic
//        )


        val file = PlatformFile(FileKit.cacheDir, DEFAULT_FILE_NAME)
        val vaultModel = VaultModel(
            items = emptyList()
        )
        val vaultRawContent = json.encodeToString(vaultModel)

        val path = file.absolutePath()
//        val store: KStore<VaultModel> = storeOf(
//            file = Path(path),
//            enableCache = false,
//            json = json
//        )
//
//        store.set(vaultModel)
//        store.close()

        // TODO add encryption logic here using the provided password
        file.writeString(vaultRawContent)

        println(file.absolutePath())
//        val file2 = PlatformFile(FileKit.cacheDir, DEFAULT_FILE_NAME)
//        println(file2.readString())
//
//        return file2.exists() // Return true if successful, false otherwise
        return true
    }

    fun changePassword(oldPassword: String, newPassword: String): Boolean {
        // Logic to change the vault password from oldPassword to newPassword
        return true // Return true if successful, false otherwise
    }

    suspend fun getVault(password: String): VaultModel {
        val file = PlatformFile(FileKit.cacheDir, DEFAULT_FILE_NAME)
//        val path = file.absolutePath()
//        val store: KStore<VaultModel> = storeOf(
//            file = Path(path),
//            json = json
//        )
//        println(file.absolutePath())
        if (!file.exists()) {
            return VaultModel(items = emptyList())
        }
        val vaultRawContent = file.readString()
        return json.decodeFromString(vaultRawContent)
//        return store.get() ?: VaultModel(items = emptyList()).also {
//            store.close()
//        }
    }

    suspend fun saveVault(vaultModel: VaultModel, password: String): Boolean {
        val file = PlatformFile(FileKit.cacheDir, DEFAULT_FILE_NAME)
        val vaultRawContent = json.encodeToString(vaultModel)
        // TODO add encryption logic here if needed
        file.writeString(vaultRawContent)
        return true // Return true if successful, false otherwise
    }
}

