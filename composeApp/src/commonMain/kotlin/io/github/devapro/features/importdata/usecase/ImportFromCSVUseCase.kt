package io.github.devapro.features.importdata.usecase

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.data.vault.VaultAdditionalFieldModel
import io.github.devapro.data.vault.VaultFileRepository
import io.github.devapro.data.vault.VaultItemModel
import io.github.devapro.data.vault.VaultItemTag
import io.github.devapro.data.vault.VaultModel
import io.github.devapro.data.vault.VaultRuntimeRepository
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readString

class ImportFromCSVUseCase(
    private val repository: VaultRuntimeRepository,
    private val fileRepository: VaultFileRepository
) {

    suspend fun execute(
        file: PlatformFile,
        password: String
    ): AppResult<Unit> {
        val fileContent = file.readString()
        return try {
            val items = parseCsv(fileContent)
            val model = VaultModel(
                items = items,
                password = password
            )
            repository.loadVault(model)
            fileRepository.saveVault(model)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    private fun parseCsv(content: String): List<VaultItemModel> {
        val lines = content.lines()
        val items = mutableListOf<VaultItemModel>()

        for (line in lines.drop(1)) { // Skip header
            val parts = line.split(",").map { it.trim('"') }
            if (parts.size < 6) continue // Ensure we have enough parts

            val item = VaultItemModel(
                id = parts.hashCode().toString(),
                title = parts[0],
                description = parts[1],
                url = parts[2],
                username = parts[3],
                password = parts[4],
                additionalFields = parseAdditionalFields(parts[5]),
                tags = parseTags(parts.getOrNull(6) ?: "")
            )
            items.add(item)
        }

        return items
    }

    private fun parseAdditionalFields(field: String): List<VaultAdditionalFieldModel> {
        return field.split(";").mapNotNull {
            val kv = it.split(":")
            if (kv.size == 2) VaultAdditionalFieldModel(kv[0].trim(), kv[1].trim()) else null
        }
    }

    private fun parseTags(field: String): List<VaultItemTag> {
        return field.split(";")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map {
                VaultItemTag(
                    it, it
                )
            }
    }
}