package io.github.devapro.ui.unlock.model

sealed interface UnLockVaultScreenAction {
    data object InitScreen : UnLockVaultScreenAction

    data class OnPasswordChanged(val password: String) : UnLockVaultScreenAction
    
    data object OnTogglePasswordVisibility : UnLockVaultScreenAction

    data object OnUnlockClicked : UnLockVaultScreenAction

    data object OnBackClicked : UnLockVaultScreenAction

    data class UnlockVault(
        val password: String
    ) : UnLockVaultScreenAction
} 