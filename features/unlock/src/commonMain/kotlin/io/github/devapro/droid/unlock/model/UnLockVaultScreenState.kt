package io.github.devapro.droid.unlock.model

sealed interface UnLockVaultScreenState {
    data object Loading : UnLockVaultScreenState

    data class Loaded(
        val vaultId: String?,
        val vaultName: String,
        val password: String,
        val isPasswordVisible: Boolean,
        val isProcessing: Boolean,
        val passwordError: String?,
        val isValid: Boolean
    ) : UnLockVaultScreenState
}
