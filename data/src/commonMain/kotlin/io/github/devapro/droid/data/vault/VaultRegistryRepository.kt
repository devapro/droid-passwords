package io.github.devapro.droid.data.vault

import io.github.devapro.droid.data.LocalStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json

class VaultRegistryRepository(
    private val localStorage: LocalStorage,
    private val json: Json,
) {

    // Guards the read-modify-write of the deleted-id set, which is mutated from both the
    // delete reducer (no syncMutex) and the background sync flush concurrently.
    private val deletedIdsMutex = Mutex()

    companion object {
        private const val KEY_VAULT_REGISTRY = "vault_registry"
        private const val KEY_ACTIVE_VAULT_ID = "active_vault_id"
        private const val KEY_DELETED_VAULT_IDS = "deleted_vault_ids"
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

    // --- Deleted-vault tombstones -----------------------------------------
    // Ids of vaults removed locally whose deletion still needs to be (or has been)
    // propagated to the server. Persisted so a deletion made offline survives and is
    // retried, and so discovery never resurrects a vault the user has deleted.

    suspend fun getDeletedVaultIds(): Set<String> =
        decodeIds(localStorage.getStringOnce(KEY_DELETED_VAULT_IDS))

    suspend fun addDeletedVaultId(id: String) = deletedIdsMutex.withLock {
        writeDeletedVaultIds(getDeletedVaultIds() + id)
    }

    suspend fun removeDeletedVaultId(id: String) = deletedIdsMutex.withLock {
        writeDeletedVaultIds(getDeletedVaultIds() - id)
    }

    private suspend fun writeDeletedVaultIds(ids: Set<String>) {
        localStorage.saveString(KEY_DELETED_VAULT_IDS, json.encodeToString(ids.toList()))
    }

    private fun decodeIds(raw: String): Set<String> {
        if (raw.isEmpty()) return emptySet()
        return try {
            json.decodeFromString<List<String>>(raw).toSet()
        } catch (_: Exception) {
            emptySet()
        }
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
