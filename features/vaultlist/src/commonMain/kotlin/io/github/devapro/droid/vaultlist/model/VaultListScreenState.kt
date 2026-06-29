package io.github.devapro.droid.vaultlist.model

import io.github.devapro.droid.data.vault.VaultDescriptor

sealed interface VaultListScreenState {
    data object Loading : VaultListScreenState

    data class Loaded(
        val vaults: List<VaultListItem>,
        val activeVaultId: String?,
        val showAddMenu: Boolean = false,
        val isLoggedIn: Boolean = false,
    ) : VaultListScreenState
}

data class VaultListItem(
    val descriptor: VaultDescriptor,
    val isLoaded: Boolean,
)
