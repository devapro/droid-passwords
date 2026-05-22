package io.github.devapro.droid.importdata.usecase

import io.github.devapro.droid.data.vault.VaultItemModel
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import io.github.devapro.droid.importdata.model.ImportConflictReport

class ComputeImportConflictsUseCase(
    private val runtimeRepository: VaultRuntimeRepository,
) {

    fun execute(parsed: List<VaultItemModel>): ImportConflictReport {
        val existingActive = runCatching { runtimeRepository.getActiveVault().items }.getOrElse { emptyList() }
        val existingKeys = existingActive.map { matchKey(it) }.toSet()
        val matched = parsed.filter { matchKey(it) in existingKeys }
        val fresh = parsed.filter { matchKey(it) !in existingKeys }
        return ImportConflictReport(parsed = parsed, matched = matched, fresh = fresh)
    }

    private fun matchKey(item: VaultItemModel): String =
        "${item.title.trim().lowercase()}|${item.username.trim().lowercase()}"
}
