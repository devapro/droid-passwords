package io.github.devapro.ui

import androidx.compose.runtime.*
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
}