package io.github.devapro.features.importexport.usecase

import io.github.devapro.core.mvi.AppResult
import io.github.devapro.data.vault.VaultItemModel
import io.github.devapro.data.vault.VaultRuntimeRepository
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.writeString

class SaveCSVFileUseCase(
    private val repository: VaultRuntimeRepository
) {

    suspend fun execute(
        file: PlatformFile
    ): AppResult<Unit> {
        return try {
            val vaultRawContent = exportToCsv(repository.getVault().items)
            file.writeString(vaultRawContent)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    private fun exportToCsv(items: List<VaultItemModel>): String {
        val csvBuilder = StringBuilder()
        csvBuilder.appendLine("Title,Description,URL,Username,Password,Additional Fields,Tags")

        items.forEach { item ->
            val additionalFieldsStr =
                item.additionalFields.joinToString(";") { "${it.name}:${it.value}" }
            val tagsStr = item.tags.joinToString(";") { it.title }
            csvBuilder.appendLine(
                "\"${escapeCsv(item.title)}\",\"${escapeCsv(item.description)}\",\"${
                    escapeCsv(
                        item.url
                    )
                }\",\"${escapeCsv(item.username)}\",\"${escapeCsv(item.password)}\",\"${
                    escapeCsv(
                        additionalFieldsStr
                    )
                }\",\"${escapeCsv(tagsStr)}\""
            )
        }

        return csvBuilder.toString()
    }

    private fun escapeCsv(value: String?): String {
        if (value.isNullOrEmpty()) return ""
        return value.replace("\"", "\"\"")
    }
}