package io.github.devapro.droid.vaultlist.model

sealed interface VaultListScreenEvent {
    data class NavigateToUnlock(val vaultId: String) : VaultListScreenEvent
    data object NavigateToTags : VaultListScreenEvent
    data object NavigateToCreate : VaultListScreenEvent
    data object NavigateToImport : VaultListScreenEvent
    data object NavigateBack : VaultListScreenEvent
    data class ShowError(val message: String) : VaultListScreenEvent
    data class ShowMessage(val message: String) : VaultListScreenEvent
}
