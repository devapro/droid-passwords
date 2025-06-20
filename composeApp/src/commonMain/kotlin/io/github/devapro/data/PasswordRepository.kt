package io.github.devapro.data

import androidx.compose.runtime.mutableStateListOf
import io.github.devapro.model.ItemModel

object PasswordRepository {
    private val _passwords = mutableStateListOf<ItemModel>()
    val passwords: List<ItemModel> get() = _passwords.toList()
    
    fun addPassword(password: ItemModel) {
        _passwords.add(password)
    }
    
    fun updatePassword(password: ItemModel) {
        val index = _passwords.indexOfFirst { it.id == password.id }
        if (index != -1) {
            _passwords[index] = password
        }
    }
    
    fun deletePassword(passwordId: String) {
        _passwords.removeAll { it.id == passwordId }
    }
    
    fun getPasswordById(id: String): ItemModel? {
        return _passwords.find { it.id == id }
    }
} 