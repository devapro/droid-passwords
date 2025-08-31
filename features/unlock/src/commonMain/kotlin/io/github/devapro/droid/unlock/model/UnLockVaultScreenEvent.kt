package io.github.devapro.droid.unlock.model

sealed interface UnLockVaultScreenEvent {

    data object NavigateBack : UnLockVaultScreenEvent

    data class ShowError(val error: String) : UnLockVaultScreenEvent

    data object UnlockSuccess : UnLockVaultScreenEvent
} 