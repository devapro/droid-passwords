package io.github.devapro.ui.unlock.model

sealed interface UnLockVaultScreenState {
    data object Loading : UnLockVaultScreenState

    data class Loaded(
        val password: String,
        val isPasswordVisible: Boolean,
        val isProcessing: Boolean,
        val passwordError: String?,
        val isValid: Boolean
    ) : UnLockVaultScreenState
} 