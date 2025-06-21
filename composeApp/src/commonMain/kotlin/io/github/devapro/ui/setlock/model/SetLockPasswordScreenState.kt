package io.github.devapro.ui.setlock.model

sealed interface SetLockPasswordScreenState {
    data object Loading : SetLockPasswordScreenState

    data class Error(val message: String) : SetLockPasswordScreenState

    data class Success(
        val hasExistingPassword: Boolean,
        val currentPassword: String,
        val newPassword: String,
        val confirmPassword: String,
        val isCurrentPasswordVisible: Boolean,
        val isNewPasswordVisible: Boolean,
        val isConfirmPasswordVisible: Boolean,
        val isProcessing: Boolean,
        val currentPasswordError: String?,
        val newPasswordError: String?,
        val confirmPasswordError: String?,
        val isValid: Boolean
    ) : SetLockPasswordScreenState
} 