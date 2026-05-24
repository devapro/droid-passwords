package io.github.devapro.droid.data.vault

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class VaultRuntimeRepository {

    private data class Entry(val descriptor: VaultDescriptor, var vault: VaultModel)

    private val loaded = mutableMapOf<String, Entry>()
    private var activeVaultId: String? = null

    private val _activeVaultChanges = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val activeVaultChanges: SharedFlow<Unit> = _activeVaultChanges.asSharedFlow()

    fun loadVault(descriptor: VaultDescriptor, vault: VaultModel) {
        loaded[descriptor.id] = Entry(descriptor, vault)
        if (activeVaultId == null) activeVaultId = descriptor.id
        _activeVaultChanges.tryEmit(Unit)
    }

    fun setActiveVault(id: String): Boolean {
        if (!loaded.containsKey(id)) return false
        activeVaultId = id
        _activeVaultChanges.tryEmit(Unit)
        return true
    }

    fun getActiveVaultId(): String? = activeVaultId

    fun isLoaded(id: String): Boolean = loaded.containsKey(id)

    fun unloadVault(id: String) {
        loaded.remove(id)
        if (activeVaultId == id) {
            activeVaultId = loaded.keys.firstOrNull()
            _activeVaultChanges.tryEmit(Unit)
        }
    }

    fun unloadAll() {
        loaded.clear()
        activeVaultId = null
        _activeVaultChanges.tryEmit(Unit)
    }

    fun getVault(): VaultModel = getActiveVault()

    fun getActiveVault(): VaultModel = requireActiveEntry().vault

    fun getActiveDescriptor(): VaultDescriptor = requireActiveEntry().descriptor

    fun getVault(id: String): VaultModel? = loaded[id]?.vault

    fun getDescriptor(id: String): VaultDescriptor? = loaded[id]?.descriptor

    fun listLoaded(): List<VaultDescriptor> = loaded.values.map { it.descriptor }

    fun replaceActiveVault(vault: VaultModel) {
        val entry = requireActiveEntry()
        entry.vault = vault
        _activeVaultChanges.tryEmit(Unit)
    }

    fun replaceActiveDescriptor(descriptor: VaultDescriptor) {
        val entry = requireActiveEntry()
        val oldId = entry.descriptor.id
        if (oldId != descriptor.id) {
            loaded.remove(oldId)
            activeVaultId = descriptor.id
        }
        loaded[descriptor.id] = entry.copy(descriptor = descriptor)
    }

    fun addOrUpdateVault(item: VaultItemModel) {
        val current = getActiveVault()
        val newItems = if (current.items.any { it.id == item.id }) {
            current.items.map { if (it.id == item.id) item else it }
        } else {
            current.items + item
        }
        replaceActiveVault(current.copy(items = newItems, updatedAt = nowMillis()))
    }

    fun deleteVaultById(itemId: String) {
        val current = getActiveVault()
        val newItems = current.items.filter { it.id != itemId }
        replaceActiveVault(current.copy(items = newItems, updatedAt = nowMillis()))
    }

    fun getAllTags(): List<VaultItemTag> =
        getActiveVault().items.flatMap { it.tags }.distinct()

    private fun requireActiveEntry(): Entry {
        val id = activeVaultId ?: error("No active vault loaded")
        return loaded[id] ?: error("Active vault id '$id' is not in the loaded map")
    }

    private fun nowMillis(): Long {
        @OptIn(kotlin.time.ExperimentalTime::class)
        return kotlin.time.Clock.System.now().toEpochMilliseconds()
    }
}
