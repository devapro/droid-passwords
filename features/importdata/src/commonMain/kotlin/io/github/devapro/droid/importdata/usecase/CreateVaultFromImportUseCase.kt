package io.github.devapro.droid.importdata.usecase

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.data.vault.VaultDescriptor
import io.github.devapro.droid.data.vault.VaultFileRepository
import io.github.devapro.droid.data.vault.VaultItemModel
import io.github.devapro.droid.data.vault.VaultModel
import io.github.devapro.droid.data.vault.VaultRegistryRepository
import io.github.devapro.droid.data.vault.VaultRuntimeRepository

class CreateVaultFromImportUseCase(
    private val fileRepository: VaultFileRepository,
    private val registryRepository: VaultRegistryRepository,
    private val runtimeRepository: VaultRuntimeRepository,
) {

    suspend fun execute(
        items: List<VaultItemModel>,
        name: String,
        password: String,
    ): AppResult<VaultDescriptor> {
        return try {
            val resolvedName = name.ifBlank { defaultName() }
            val now = nowMillis()
            val id = VaultDescriptor.newId()
            val descriptor = VaultDescriptor(
                id = id,
                name = resolvedName,
                fileName = VaultDescriptor.newFileName(id),
                createdAt = now,
                updatedAt = now,
            )
            val vault = VaultModel(
                password = password,
                items = items,
                name = resolvedName,
                createdAt = now,
                updatedAt = now,
            )
            when (val save = fileRepository.saveVault(descriptor, vault)) {
                is AppResult.Success -> {
                    registryRepository.addVault(descriptor)
                    registryRepository.setActiveVaultId(descriptor.id)
                    runtimeRepository.loadVault(descriptor, vault)
                    runtimeRepository.setActiveVault(descriptor.id)
                    AppResult.Success(descriptor)
                }
                is AppResult.Failure -> AppResult.Failure(save.error)
            }
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    private fun defaultName(): String = "Imported vault"

    private fun nowMillis(): Long {
        @OptIn(kotlin.time.ExperimentalTime::class)
        return kotlin.time.Clock.System.now().toEpochMilliseconds()
    }
}
