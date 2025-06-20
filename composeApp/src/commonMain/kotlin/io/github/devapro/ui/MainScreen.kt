package io.github.devapro.ui

import androidx.compose.runtime.*
import io.github.devapro.data.ImportExportManager
import io.github.devapro.data.ImportException
import io.github.devapro.data.PasswordRepository
import io.github.devapro.model.ItemModel

@Composable
fun MainScreen() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.PasswordList) }
    var editingItem by remember { mutableStateOf<ItemModel?>(null) }
    var viewingItem by remember { mutableStateOf<ItemModel?>(null) }
    val passwords by remember { derivedStateOf { PasswordRepository.passwords } }
    
    when (val screen = currentScreen) {
        is Screen.PasswordList -> {
            PasswordListScreen(
                items = passwords,
                onAddClick = {
                    editingItem = null
                    currentScreen = Screen.AddEditPassword
                },
                onEditClick = { item ->
                    viewingItem = item
                    currentScreen = Screen.PasswordDetail
                },
                onImportExportClick = {
                    currentScreen = Screen.ImportExport
                }
            )
        }
        
        is Screen.PasswordDetail -> {
            viewingItem?.let { item ->
                PasswordDetailScreen(
                    item = item,
                    onBack = {
                        currentScreen = Screen.PasswordList
                    },
                    onEdit = {
                        editingItem = item
                        currentScreen = Screen.AddEditPassword
                    }
                )
            }
        }
        
        is Screen.ImportExport -> {
            ImportExportScreen(
                onBack = {
                    currentScreen = Screen.PasswordList
                },
                onImport = { format ->
                    // For now, we'll simulate file content - in a real app, you'd use a file picker
                    val simulatedContent = when (format) {
                        FileFormat.JSON -> """{
  "passwords": [
    {
      "id": "1",
      "title": "Sample Account",
      "description": "This is a sample imported password",
      "url": "https://example.com",
      "username": "user@example.com",
      "password": "samplepass123",
      "additionalFields": [
        {
          "id": "1",
          "name": "Security Question",
          "value": "Mother's maiden name"
        }
      ]
    }
  ]
}"""
                        FileFormat.CSV -> """Title,Description,URL,Username,Password,Additional Fields
"Sample Account","This is a sample imported password","https://example.com","user@example.com","samplepass123","Security Question:Mother's maiden name"
"Another Account","Second sample","https://test.com","test@test.com","password456",""""
                        FileFormat.XML -> """<?xml version="1.0" encoding="UTF-8"?>
<passwords>
  <password>
    <id>1</id>
    <title>Sample Account</title>
    <description>This is a sample imported password</description>
    <url>https://example.com</url>
    <username>user@example.com</username>
    <password>samplepass123</password>
    <additionalFields>
      <field>
        <id>1</id>
        <name>Security Question</name>
        <value>Mother's maiden name</value>
      </field>
    </additionalFields>
  </password>
</passwords>"""
                    }
                    
                    try {
                        val importedItems = ImportExportManager.importFromString(simulatedContent, format)
                        importedItems.forEach { item ->
                            PasswordRepository.addPassword(item)
                        }
                        currentScreen = Screen.PasswordList
                    } catch (e: ImportException) {
                        // In a real app, you'd show an error dialog
                        println("Import failed: ${e.message}")
                    }
                },
                onExport = { format ->
                    try {
                        val exportedContent = ImportExportManager.exportToString(passwords, format)
                        // In a real app, you'd use a file saver dialog
                        println("Exported data in ${format.name} format:")
                        println(exportedContent)
                        currentScreen = Screen.PasswordList
                    } catch (e: Exception) {
                        // In a real app, you'd show an error dialog
                        println("Export failed: ${e.message}")
                    }
                }
            )
        }
        
        is Screen.AddEditPassword -> {
            AddEditPasswordScreen(
                item = editingItem,
                onSave = { password ->
                    if (editingItem != null) {
                        PasswordRepository.updatePassword(password)
                    } else {
                        PasswordRepository.addPassword(password)
                    }
                    currentScreen = Screen.PasswordList
                },
                onBack = {
                    currentScreen = Screen.PasswordList
                }
            )
        }
    }
}

sealed class Screen {
    object PasswordList : Screen()
    object PasswordDetail : Screen()
    object AddEditPassword : Screen()
    object ImportExport : Screen()
}