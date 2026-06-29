package io.github.devapro.droid.vaultlist.model

import io.github.devapro.droid.data.vault.VaultDescriptor

sealed interface VaultListScreenAction {
    data object InitScreen : VaultListScreenAction
    data object OnRefresh : VaultListScreenAction
    data object OnBackClicked : VaultListScreenAction
    data object OnAddVaultClicked : VaultListScreenAction
    data object OnDismissAddMenu : VaultListScreenAction
    data object OnCreateNewVaultSelected : VaultListScreenAction
    data object OnImportVaultSelected : VaultListScreenAction
    data object OnLogoutClicked : VaultListScreenAction
    data class OnVaultClicked(val descriptor: VaultDescriptor) : VaultListScreenAction
}
