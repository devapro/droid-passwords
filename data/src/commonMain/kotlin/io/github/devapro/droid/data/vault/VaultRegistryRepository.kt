package io.github.devapro.droid.data.vault

import io.github.devapro.droid.data.LocalStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class VaultRegistryRepository(
    private val localStorage: LocalStorage,
    private val json: Json,
) {

    companion object {
        private const val KEY_VAULT_REGISTRY = "vault_registry"
        private const val KEY_ACTIVE_VAULT_ID = "active_vault_id"
    }

    fun observeRegistry(): Flow<List<VaultDescriptor>> =
        localStorage.getString(KEY_VAULT_REGISTRY).map { decodeRegistry(it) }

    suspend fun getRegistry(): List<VaultDescriptor> = readRegistry()

    suspend fun getDescriptor(id: String): VaultDescriptor? =
        readRegistry().firstOrNull { it.id == id }

    suspend fun addVault(descriptor: VaultDescriptor) {
        val current = readRegistry().filterNot { it.id == descriptor.id }
        writeRegistry(current + descriptor)
    }

    suspend fun renameVault(id: String, name: String) {
        val current = readRegistry()
        val now = currentTimeMillis()
        val updated = current.map {
            if (it.id == id) it.copy(name = name, updatedAt = now) else it
        }
        writeRegistry(updated)
    }

    suspend fun removeVault(id: String) {
        val current = readRegistry().filterNot { it.id == id }
        writeRegistry(current)
        if (getActiveVaultId() == id) {
            setActiveVaultId(current.firstOrNull()?.id)
        }
    }

    suspend fun getActiveVaultId(): String? {
        val stored = localStorage.getStringOnce(KEY_ACTIVE_VAULT_ID)
        return stored.takeIf { it.isNotEmpty() }
    }

    suspend fun setActiveVaultId(id: String?) {
        localStorage.saveString(KEY_ACTIVE_VAULT_ID, id.orEmpty())
    }

    suspend fun requireActiveDescriptor(): VaultDescriptor {
        val id = getActiveVaultId() ?: error("No active vault set in registry")
        return getDescriptor(id) ?: error("Active vault id '$id' not present in registry")
    }

    private suspend fun readRegistry(): List<VaultDescriptor> =
        decodeRegistry(localStorage.getStringOnce(KEY_VAULT_REGISTRY))

    private suspend fun writeRegistry(list: List<VaultDescriptor>) {
        localStorage.saveString(KEY_VAULT_REGISTRY, json.encodeToString(list))
    }

    private fun decodeRegistry(raw: String): List<VaultDescriptor> {
        if (raw.isEmpty()) return emptyList()
        return try {
            json.decodeFromString(raw)
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun currentTimeMillis(): Long {
        @OptIn(kotlin.time.ExperimentalTime::class)
        return kotlin.time.Clock.System.now().toEpochMilliseconds()
    }
}
