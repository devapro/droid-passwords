package io.github.devapro.ui.unlock.model

sealed interface UnLockVaultScreenEvent {

    data object NavigateBack : UnLockVaultScreenEvent

    data class ShowError(val error: String) : UnLockVaultScreenEvent

    data object UnlockSuccess : UnLockVaultScreenEvent
} 