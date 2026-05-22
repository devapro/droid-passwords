package io.github.devapro.droid.importdata.usecase

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.data.vault.VaultAdditionalFieldModel
import io.github.devapro.droid.data.vault.VaultFileRepository
import io.github.devapro.droid.data.vault.VaultItemModel
import io.github.devapro.droid.data.vault.VaultItemTag
import io.github.devapro.droid.data.vault.VaultModel
import io.github.devapro.droid.importdata.model.FileFormat
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readString
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class ParsedImport(
    val items: List<VaultItemModel>,
    val embeddedVaultName: String? = null,
)

class ParseImportFileUseCase(
    private val fileRepository: VaultFileRepository,
) {

    suspend fun execute(
        file: PlatformFile,
        format: FileFormat,
        password: String?,
    ): AppResult<ParsedImport> {
        return try {
            when (format) {
                FileFormat.CSV -> AppResult.Success(ParsedImport(items = parseCsv(file.readString())))
                FileFormat.JSON -> AppResult.Success(ParsedImport(items = parseJson(file.readString())))
                FileFormat.DATA -> {
                    val pw = password.orEmpty()
                    if (pw.isBlank()) {
                        return AppResult.Failure(Exception("Password is required for encrypted vault files."))
                    }
                    when (val decrypted = fileRepository.getVaultFromExternalFile(file, pw)) {
                        is AppResult.Success -> {
                            val v: VaultModel = decrypted.value
                            AppResult.Success(
                                ParsedImport(
                                    items = v.items.map { it.withFreshIdIfBlank() },
                                    embeddedVaultName = v.name.takeIf { it.isNotBlank() },
                                )
                            )
                        }
                        is AppResult.Failure -> AppResult.Failure(decrypted.error)
                    }
                }
            }
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun VaultItemModel.withFreshIdIfBlank(): VaultItemModel =
        if (id.isBlank()) copy(id = Uuid.random().toHexDashString()) else this

    @OptIn(ExperimentalUuidApi::class)
    private fun parseCsv(content: String): List<VaultItemModel> {
        val items = mutableListOf<VaultItemModel>()
        for (line in content.lines().drop(1)) {
            val parts = line.split(",").map { it.trim('"') }
            if (parts.size < 6) continue
            items += VaultItemModel(
                id = Uuid.random().toHexDashString(),
                title = parts[0],
                description = parts[1],
                url = parts[2],
                username = parts[3],
                password = parts[4],
                additionalFields = parseAdditionalFields(parts[5]),
                tags = parseTags(parts.getOrNull(6) ?: ""),
            )
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
            .map { VaultItemTag(it, it) }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun parseJson(content: String): List<VaultItemModel> {
        val items = mutableListOf<VaultItemModel>()

        val cleanContent = content.trim()
        val passwordsStart = cleanContent.indexOf("\"passwords\":")
        if (passwordsStart == -1) return emptyList()

        val arrayStart = cleanContent.indexOf("[", passwordsStart)
        val arrayEnd = cleanContent.lastIndexOf("]")
        if (arrayStart == -1 || arrayEnd == -1) return emptyList()

        val passwordsArray = cleanContent.substring(arrayStart + 1, arrayEnd)
        splitJsonObjects(passwordsArray).forEach { obj ->
            val title = extractJsonValue(obj, "title")
            val description = extractJsonValue(obj, "description")
            val url = extractJsonValue(obj, "url")
            val username = extractJsonValue(obj, "username")
            val itemPassword = extractJsonValue(obj, "password")

            val additionalFields = mutableListOf<VaultAdditionalFieldModel>()
            val fieldsStart = obj.indexOf("\"additionalFields\":")
            if (fieldsStart != -1) {
                val fieldsArrayStart = obj.indexOf("[", fieldsStart)
                val fieldsArrayEnd = obj.indexOf("]", fieldsArrayStart)
                if (fieldsArrayStart != -1 && fieldsArrayEnd != -1) {
                    val fieldsArray = obj.substring(fieldsArrayStart + 1, fieldsArrayEnd)
                    splitJsonObjects(fieldsArray).forEach { fieldObj ->
                        val name = extractJsonValue(fieldObj, "name")
                        val value = extractJsonValue(fieldObj, "value")
                        if (name.isNotBlank() || value.isNotBlank()) {
                            additionalFields += VaultAdditionalFieldModel(name = name, value = value)
                        }
                    }
                }
            }

            val tags = mutableListOf<VaultItemTag>()
            val tagsStart = obj.indexOf("\"tags\":")
            if (tagsStart != -1) {
                val tagsArrayStart = obj.indexOf("[", tagsStart)
                val tagsArrayEnd = obj.indexOf("]", tagsArrayStart)
                if (tagsArrayStart != -1 && tagsArrayEnd != -1) {
                    val tagsArray = obj.substring(tagsArrayStart + 1, tagsArrayEnd)
                    splitJsonObjects(tagsArray).forEach { tagObj ->
                        tags += VaultItemTag(
                            id = tagObj.replace("\\\"", ""),
                            title = tagObj.replace("\\\"", ""),
                        )
                    }
                }
            }

            items += VaultItemModel(
                id = Uuid.random().toHexDashString(),
                title = title,
                description = description,
                url = url,
                username = username,
                password = itemPassword,
                additionalFields = additionalFields,
                tags = tags,
            )
        }
        return items
    }

    private fun extractJsonValue(json: String, key: String): String {
        val keyStart = json.indexOf("\"$key\":")
        if (keyStart == -1) return ""
        val valueStart = json.indexOf("\"", keyStart + key.length + 3)
        if (valueStart == -1) return ""
        val valueEnd = json.indexOf("\"", valueStart + 1)
        return if (valueEnd != -1) json.substring(valueStart + 1, valueEnd) else ""
    }

    private fun splitJsonObjects(jsonArray: String): List<String> {
        val objects = mutableListOf<String>()
        var braceCount = 0
        var start = -1
        for (i in jsonArray.indices) {
            when (jsonArray[i]) {
                '{' -> {
                    if (braceCount == 0) start = i
                    braceCount++
                }
                '}' -> {
                    braceCount--
                    if (braceCount == 0 && start != -1) {
                        objects += jsonArray.substring(start, i + 1)
                        start = -1
                    }
                }
            }
        }
        return objects
    }
}
