package io.github.devapro.data

import androidx.compose.runtime.mutableStateOf
import io.github.devapro.model.LockInterval
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object LockManager {
    private var _lockPassword = mutableStateOf<String?>(null)
    private var _isLocked = mutableStateOf(true)
    private var _isFirstLaunch = mutableStateOf(true)
    private var _lockInterval = mutableStateOf(LockInterval.NEVER)

    private var lockTimerJob: Job? = null
    private val lockScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    
    val isLocked: Boolean get() = _isLocked.value
    val hasLockPassword: Boolean get() = !_lockPassword.value.isNullOrBlank()
    val isFirstLaunch: Boolean get() = _isFirstLaunch.value
    val lockInterval: LockInterval get() = _lockInterval.value
    
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
            startLockTimer() // Start timer when unlocked
            return true
        }
        return false
    }
    
    fun lock() {
        _isLocked.value = true
        stopLockTimer() // Stop timer when locked
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

    /**
     * Sets the lock interval configuration
     */
    fun setLockInterval(interval: LockInterval) {
        _lockInterval.value = interval
        // Restart timer with new interval if currently running
        if (lockTimerJob?.isActive == true) {
            stopLockTimer()
            startLockTimer()
        }
    }

    /**
     * Starts the lock timer based on the current lock interval
     * Only starts if interval is not NEVER and app is unlocked
     */
    fun startLockTimer() {
        // Don't start timer if already locked, no password set, or interval is NEVER
        if (_isLocked.value || !hasLockPassword || _lockInterval.value == LockInterval.NEVER) {
            return
        }

        // Cancel existing timer
        stopLockTimer()

        // Start new timer
        lockTimerJob = lockScope.launch {
            delay(_lockInterval.value.minutes * 60 * 1000L) // Convert minutes to milliseconds
            if (!_isLocked.value) { // Double-check we're still unlocked
                lock()
            }
        }
    }

    /**
     * Stops the current lock timer
     */
    fun stopLockTimer() {
        lockTimerJob?.cancel()
        lockTimerJob = null
    }

    /**
     * Resets the lock timer by stopping and starting it again
     * Used when user activity is detected
     */
    fun resetLockTimer() {
        if (_lockInterval.value != LockInterval.NEVER && !_isLocked.value && hasLockPassword) {
            startLockTimer()
        }
    }
} 