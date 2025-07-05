package io.github.devapro.data

import io.github.devapro.model.AdditionalFieldsModel
import io.github.devapro.model.ItemModel
import kotlinx.uuid.UUID
import kotlinx.uuid.generateUUID

object ImportExportManager {
    
    fun exportToString(items: List<ItemModel>, format: FileFormat): String {
        return when (format) {
            FileFormat.CSV -> exportToCsv(items)
            FileFormat.XML -> exportToXml(items)
            FileFormat.JSON -> exportToJson(items)
        }
    }
    
    fun importFromString(content: String, format: FileFormat): List<ItemModel> {
        return try {
            when (format) {
                FileFormat.CSV -> importFromCsv(content)
                FileFormat.XML -> importFromXml(content)
                FileFormat.JSON -> importFromJson(content)
            }
        } catch (e: Exception) {
            throw ImportException("Failed to import ${format.name} file: ${e.message}")
        }
    }
    
    private fun exportToCsv(items: List<ItemModel>): String {
        val csvBuilder = StringBuilder()
        csvBuilder.appendLine("Title,Description,URL,Username,Password,Additional Fields,Tags")
        
        items.forEach { item ->
            val additionalFieldsStr = item.additionalFields.joinToString(";") { "${it.name}:${it.value}" }
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
    
    private fun exportToXml(items: List<ItemModel>): String {
        val xmlBuilder = StringBuilder()
        xmlBuilder.appendLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
        xmlBuilder.appendLine("<passwords>")
        
        items.forEach { item ->
            xmlBuilder.appendLine("  <password>")
            xmlBuilder.appendLine("    <id>${escapeXml(item.id)}</id>")
            xmlBuilder.appendLine("    <title>${escapeXml(item.title)}</title>")
            xmlBuilder.appendLine("    <description>${escapeXml(item.description)}</description>")
            xmlBuilder.appendLine("    <url>${escapeXml(item.url)}</url>")
            xmlBuilder.appendLine("    <username>${escapeXml(item.username)}</username>")
            xmlBuilder.appendLine("    <password>${escapeXml(item.password)}</password>")
            xmlBuilder.appendLine("    <additionalFields>")
            item.additionalFields.forEach { field ->
                xmlBuilder.appendLine("      <field>")
                xmlBuilder.appendLine("        <name>${escapeXml(field.name)}</name>")
                xmlBuilder.appendLine("        <value>${escapeXml(field.value)}</value>")
                xmlBuilder.appendLine("      </field>")
            }
            xmlBuilder.appendLine("    </additionalFields>")
            xmlBuilder.appendLine("    <tags>")
            item.tags.forEach { tag ->
                xmlBuilder.appendLine("      <tag>${escapeXml(tag.title)}</tag>")
            }
            xmlBuilder.appendLine("    </tags>")
            xmlBuilder.appendLine("  </password>")
        }
        
        xmlBuilder.appendLine("</passwords>")
        return xmlBuilder.toString()
    }
    
    private fun exportToJson(items: List<ItemModel>): String {
        val jsonBuilder = StringBuilder()
        jsonBuilder.appendLine("{")
        jsonBuilder.appendLine("  \"passwords\": [")
        
        items.forEachIndexed { index, item ->
            jsonBuilder.appendLine("    {")
            jsonBuilder.appendLine("      \"id\": \"${escapeJson(item.id)}\",")
            jsonBuilder.appendLine("      \"title\": \"${escapeJson(item.title)}\",")
            jsonBuilder.appendLine("      \"description\": \"${escapeJson(item.description)}\",")
            jsonBuilder.appendLine("      \"url\": \"${escapeJson(item.url)}\",")
            jsonBuilder.appendLine("      \"username\": \"${escapeJson(item.username)}\",")
            jsonBuilder.appendLine("      \"password\": \"${escapeJson(item.password)}\",")
            jsonBuilder.appendLine("      \"additionalFields\": [")
            
            item.additionalFields.forEachIndexed { fieldIndex, field ->
                jsonBuilder.appendLine("        {")
                jsonBuilder.appendLine("          \"name\": \"${escapeJson(field.name)}\",")
                jsonBuilder.append("          \"value\": \"${escapeJson(field.value)}\"")
                jsonBuilder.appendLine()
                jsonBuilder.append("        }")
                if (fieldIndex < item.additionalFields.size - 1) jsonBuilder.append(",")
                jsonBuilder.appendLine()
            }

            jsonBuilder.append("      ],")
            jsonBuilder.appendLine("      \"tags\": [")
            item.tags.forEachIndexed { tagIndex, tag ->
                jsonBuilder.append("        \"${escapeJson(tag.title)}\"")
                if (tagIndex < item.tags.size - 1) jsonBuilder.append(",")
                jsonBuilder.appendLine()
            }
            jsonBuilder.append("      ]")
            jsonBuilder.appendLine()
            jsonBuilder.append("    }")
            if (index < items.size - 1) jsonBuilder.append(",")
            jsonBuilder.appendLine()
        }
        
        jsonBuilder.appendLine("  ]")
        jsonBuilder.appendLine("}")
        return jsonBuilder.toString()
    }
    
    private fun importFromCsv(content: String): List<ItemModel> {
        val lines = content.lines().filter { it.isNotBlank() }
        if (lines.isEmpty()) return emptyList()
        
        val items = mutableListOf<ItemModel>()
        
        // Skip header line
        lines.drop(1).forEach { line ->
            val columns = parseCsvLine(line)
            if (columns.size >= 6) {
                val additionalFields = if (columns[5].isNotBlank()) {
                    columns[5].split(";").mapNotNull { field ->
                        val parts = field.split(":", limit = 2)
                        if (parts.size == 2) {
                            AdditionalFieldsModel(
                                name = parts[0],
                                value = parts[1]
                            )
                        } else null
                    }
                } else emptyList()

                val tags = if (columns.size >= 7 && columns[6].isNotBlank()) {
                    columns[6].split(";").map {
                        io.github.devapro.data.vault.VaultItemTag(
                            id = it,
                            title = it
                        )
                    }
                } else emptyList()

                items.add(
                    ItemModel(
                        id = UUID.generateUUID().toString(),
                        title = columns[0],
                        description = columns[1],
                        url = columns[2],
                        username = columns[3],
                        password = columns[4],
                        additionalFields = additionalFields,
                        tags = tags
                    )
                )
            }
        }
        
        return items
    }
    
    private fun importFromXml(content: String): List<ItemModel> {
        // Simple XML parsing - in a real app, you'd use a proper XML parser
        val items = mutableListOf<ItemModel>()
        val passwordBlocks = content.split("<password>").drop(1)
        
        passwordBlocks.forEach { block ->
            if (block.contains("</password>")) {
                val passwordData = block.substring(0, block.indexOf("</password>"))
                
                val title = extractXmlValue(passwordData, "title")
                val description = extractXmlValue(passwordData, "description")
                val url = extractXmlValue(passwordData, "url")
                val username = extractXmlValue(passwordData, "username")
                val password = extractXmlValue(passwordData, "password")
                
                val additionalFields = mutableListOf<AdditionalFieldsModel>()
                val fieldBlocks = passwordData.split("<field>").drop(1)
                fieldBlocks.forEach { fieldBlock ->
                    if (fieldBlock.contains("</field>")) {
                        val fieldData = fieldBlock.substring(0, fieldBlock.indexOf("</field>"))
                        val name = extractXmlValue(fieldData, "name")
                        val value = extractXmlValue(fieldData, "value")
                        if (name.isNotBlank() || value.isNotBlank()) {
                            additionalFields.add(
                                AdditionalFieldsModel(
                                    name = name,
                                    value = value
                                )
                            )
                        }
                    }
                }

                val tags = mutableListOf<io.github.devapro.data.vault.VaultItemTag>()
                val tagsBlock = extractXmlValue(passwordData, "tags")
                if (tagsBlock.isNotBlank()) {
                    val tagBlocks = tagsBlock.split("<tag>").drop(1)
                    tagBlocks.forEach { tagBlock ->
                        if (tagBlock.contains("</tag>")) {
                            val tag = tagBlock.substring(0, tagBlock.indexOf("</tag>"))
                            tags.add(
                                io.github.devapro.data.vault.VaultItemTag(
                                    id = tag,
                                    title = tag
                                )
                            )
                        }
                    }
                }
                
                items.add(
                    ItemModel(
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
        }
        
        return items
    }
    
    private fun importFromJson(content: String): List<ItemModel> {
        // Simple JSON parsing - in a real app, you'd use a proper JSON parser like kotlinx.serialization
        val items = mutableListOf<ItemModel>()
        
        try {
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
                
                val additionalFields = mutableListOf<AdditionalFieldsModel>()
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
                                    AdditionalFieldsModel(
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
                    ItemModel(
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
        } catch (e: Exception) {
            throw ImportException("Invalid JSON format")
        }
        
        return items
    }
    
    // Helper functions for escaping
    private fun escapeCsv(value: String): String {
        return value.replace("\"", "\"\"")
    }
    
    private fun escapeXml(value: String): String {
        return value
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;")
    }
    
    private fun escapeJson(value: String): String {
        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
    }
    
    // Helper functions for parsing
    private fun parseCsvLine(line: String): List<String> {
        val result = mutableListOf<String>()
        var inQuotes = false
        var current = StringBuilder()
        
        for (i in line.indices) {
            val char = line[i]
            when {
                char == '\"' && (i == 0 || line[i - 1] != '\\') -> inQuotes = !inQuotes
                char == ',' && !inQuotes -> {
                    result.add(current.toString())
                    current = StringBuilder()
                }
                else -> current.append(char)
            }
        }
        result.add(current.toString())

        return result.map { it.trim('\"') }
    }
    
    private fun extractXmlValue(xml: String, tag: String): String {
        val start = xml.indexOf("<$tag>")
        val end = xml.indexOf("</$tag>")
        return if (start != -1 && end != -1 && start < end) {
            xml.substring(start + tag.length + 2, end)
        } else ""
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

class ImportException(message: String) : Exception(message) 