package io.github.devapro.features.importdata.usecase

import io.github.devapro.core.mvi.AppResult
import io.github.devapro.data.vault.VaultAdditionalFieldModel
import io.github.devapro.data.vault.VaultItemModel
import io.github.devapro.data.vault.VaultModel
import io.github.devapro.data.vault.VaultRuntimeRepository
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readString
import kotlinx.uuid.UUID
import kotlinx.uuid.generateUUID

class ImportFromJsonUseCase(
    private val repository: VaultRuntimeRepository
) {

    suspend fun execute(
        file: PlatformFile,
        password: String
    ): AppResult<Unit> {
        val fileContent = file.readString()
        return try {
            val model = VaultModel(
                items = importFromJson(fileContent),
                password = password
            )
            repository.loadVault(model)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    private fun importFromJson(content: String): List<VaultItemModel> {
        // Simple JSON parsing - in a real app, you'd use a proper JSON parser like kotlinx.serialization
        val items = mutableListOf<VaultItemModel>()

        val cleanContent = content.trim()
        val passwordsStart = cleanContent.indexOf("\"passwords\":")
        if (passwordsStart == -1) return emptyList()

        val arrayStart = cleanContent.indexOf("[", passwordsStart)
        val arrayEnd = cleanContent.lastIndexOf("]")
        if (arrayStart == -1 || arrayEnd == -1) return emptyList()

        val passwordsArray = cleanContent.substring(arrayStart + 1, arrayEnd)
        val passwordObjects = splitJsonObjects(passwordsArray)

        passwordObjects.forEach { obj ->
            val title = extractJsonValue(obj, "title")
            val description = extractJsonValue(obj, "description")
            val url = extractJsonValue(obj, "url")
            val username = extractJsonValue(obj, "username")
            val password = extractJsonValue(obj, "password")

            val additionalFields = mutableListOf<VaultAdditionalFieldModel>()
            val fieldsStart = obj.indexOf("\"additionalFields\":")
            if (fieldsStart != -1) {
                val fieldsArrayStart = obj.indexOf("[", fieldsStart)
                val fieldsArrayEnd = obj.indexOf("]", fieldsArrayStart)
                if (fieldsArrayStart != -1 && fieldsArrayEnd != -1) {
                    val fieldsArray = obj.substring(fieldsArrayStart + 1, fieldsArrayEnd)
                    val fieldObjects = splitJsonObjects(fieldsArray)
                    fieldObjects.forEach { fieldObj ->
                        val name = extractJsonValue(fieldObj, "name")
                        val value = extractJsonValue(fieldObj, "value")
                        if (name.isNotBlank() || value.isNotBlank()) {
                            additionalFields.add(
                                VaultAdditionalFieldModel(
                                    name = name,
                                    value = value
                                )
                            )
                        }
                    }
                }
            }

            val tags = mutableListOf<io.github.devapro.data.vault.VaultItemTag>()
            val tagsStart = obj.indexOf("\"tags\":")
            if (tagsStart != -1) {
                val tagsArrayStart = obj.indexOf("[", tagsStart)
                val tagsArrayEnd = obj.indexOf("]", tagsArrayStart)
                if (tagsArrayStart != -1 && tagsArrayEnd != -1) {
                    val tagsArray = obj.substring(tagsArrayStart + 1, tagsArrayEnd)
                    val tagObjects = splitJsonObjects(tagsArray)
                    tagObjects.forEach { tagObj ->
                        tags.add(
                            io.github.devapro.data.vault.VaultItemTag(
                                id = tagObj.replace("\\\"", ""),
                                title = tagObj.replace("\\\"", "")
                            )
                        )
                    }
                }
            }

            items.add(
                VaultItemModel(
                    id = UUID.generateUUID().toString(),
                    title = title,
                    description = description,
                    url = url,
                    username = username,
                    password = password,
                    additionalFields = additionalFields,
                    tags = tags
                )
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
        return if (valueEnd != -1) {
            json.substring(valueStart + 1, valueEnd)
        } else ""
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
                        objects.add(jsonArray.substring(start, i + 1))
                        start = -1
                    }
                }
            }
        }

        return objects
    }
}