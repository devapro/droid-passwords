package io.github.devapro.ui.unlock.model

sealed interface UnLockVaultScreenState {
    data object Loading : UnLockVaultScreenState

    data class Error(val message: String) : UnLockVaultScreenState

    data class Success(
        val password: String,
        val isPasswordVisible: Boolean,
        val isProcessing: Boolean,
        val passwordError: String?,
        val isValid: Boolean
    ) : UnLockVaultScreenState
} 