package io.github.devapro.droid.unlock.model

sealed interface UnLockVaultScreenAction {
    data class InitScreen(val vaultId: String?) : UnLockVaultScreenAction

    data class OnPasswordChanged(val password: String) : UnLockVaultScreenAction

    data object OnTogglePasswordVisibility : UnLockVaultScreenAction

    data object OnUnlockClicked : UnLockVaultScreenAction

    data object OnBackClicked : UnLockVaultScreenAction

    data class UnlockVault(
        val password: String
    ) : UnLockVaultScreenAction
}
