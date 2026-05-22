package io.github.devapro.droid.importdata.usecase

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.data.vault.VaultFileRepository
import io.github.devapro.droid.data.vault.VaultItemModel
import io.github.devapro.droid.data.vault.VaultRegistryRepository
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import io.github.devapro.droid.importdata.model.ImportStrategy
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class ApplyImportResult(
    val added: Int,
    val skipped: Int,
)

class ApplyImportToActiveVaultUseCase(
    private val runtimeRepository: VaultRuntimeRepository,
    private val registryRepository: VaultRegistryRepository,
    private val fileRepository: VaultFileRepository,
) {

    @OptIn(ExperimentalUuidApi::class)
    suspend fun execute(
        items: List<VaultItemModel>,
        strategy: ImportStrategy,
    ): AppResult<ApplyImportResult> {
        return try {
            val descriptor = registryRepository.requireActiveDescriptor()
            val current = runtimeRepository.getActiveVault()
            val existingKeys = current.items.map { matchKey(it) }.toSet()

            val (newItems, added, skipped) = when (strategy) {
                ImportStrategy.REPLACE -> {
                    Triple(items, items.size, 0)
                }
                ImportStrategy.APPEND -> {
                    val withFreshIds = items.map { it.copy(id = Uuid.random().toHexDashString()) }
                    Triple(current.items + withFreshIds, withFreshIds.size, 0)
                }
                ImportStrategy.MERGE_BY_TITLE_USERNAME -> {
                    val toAdd = items.filterNot { matchKey(it) in existingKeys }
                    val withFreshIds = toAdd.map { it.copy(id = Uuid.random().toHexDashString()) }
                    Triple(current.items + withFreshIds, withFreshIds.size, items.size - withFreshIds.size)
                }
            }

            val merged = current.copy(items = newItems)
            runtimeRepository.replaceActiveVault(merged)
            when (val save = fileRepository.saveVault(descriptor, merged)) {
                is AppResult.Success -> AppResult.Success(ApplyImportResult(added = added, skipped = skipped))
                is AppResult.Failure -> AppResult.Failure(save.error)
            }
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    private fun matchKey(item: VaultItemModel): String =
        "${item.title.trim().lowercase()}|${item.username.trim().lowercase()}"
}
