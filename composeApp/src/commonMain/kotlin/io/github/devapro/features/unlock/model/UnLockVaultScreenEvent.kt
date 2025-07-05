package io.github.devapro.features.unlock.model

sealed interface UnLockVaultScreenEvent {

    data object NavigateBack : UnLockVaultScreenEvent

    data class ShowError(val error: String) : UnLockVaultScreenEvent

    data object UnlockSuccess : UnLockVaultScreenEvent
} 