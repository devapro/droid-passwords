package io.github.devapro.ui.unlock.model

sealed interface UnLockVaultScreenEvent {

    data object NavigateBack : UnLockVaultScreenEvent

    data class UnlockVault(val password: String) : UnLockVaultScreenEvent

    data object UnlockSuccess : UnLockVaultScreenEvent
} 