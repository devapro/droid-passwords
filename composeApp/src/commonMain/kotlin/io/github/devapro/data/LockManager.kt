package io.github.devapro.data

import androidx.compose.runtime.mutableStateOf

object LockManager {
    private var _lockPassword = mutableStateOf<String?>(null)
    private var _isLocked = mutableStateOf(true)
    private var _isFirstLaunch = mutableStateOf(true)
    
    val isLocked: Boolean get() = _isLocked.value
    val hasLockPassword: Boolean get() = !_lockPassword.value.isNullOrBlank()
    val isFirstLaunch: Boolean get() = _isFirstLaunch.value
    
    fun setLockPassword(password: String) {
        _lockPassword.value = password
        _isLocked.value = true
        _isFirstLaunch.value = false
    }
    
    fun validatePassword(password: String): Boolean {
        return _lockPassword.value == password
    }
    
    fun unlock(password: String): Boolean {
        if (validatePassword(password)) {
            _isLocked.value = false
            return true
        }
        return false
    }
    
    fun lock() {
        _isLocked.value = true
    }
    
    fun removeLockPassword() {
        _lockPassword.value = null
        _isLocked.value = false
    }
    
    fun completeFirstLaunch() {
        _isFirstLaunch.value = false
    }
    
    fun changeLockPassword(oldPassword: String, newPassword: String): Boolean {
        if (validatePassword(oldPassword)) {
            _lockPassword.value = newPassword
            return true
        }
        return false
    }
} 