package io.github.devapro.droid.vaultlist.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.vaultlist.model.VaultListScreenAction
import io.github.devapro.droid.vaultlist.model.VaultListScreenEvent
import io.github.devapro.droid.vaultlist.model.VaultListScreenState

class OnImportVaultSelectedReducer
    : Reducer<VaultListScreenAction.OnImportVaultSelected, VaultListScreenState, VaultListScreenAction, VaultListScreenEvent> {

    override val actionClass = VaultListScreenAction.OnImportVaultSelected::class

    override suspend fun reduce(
        action: VaultListScreenAction.OnImportVaultSelected,
        getState: () -> VaultListScreenState
    ): Reducer.Result<VaultListScreenState, VaultListScreenAction.OnImportVaultSelected, VaultListScreenEvent?> {
        val current = getState()
        val newState = (current as? VaultListScreenState.Loaded)?.copy(showAddMenu = false) ?: current
        return Reducer.Result(
            state = newState,
            action = null,
            event = VaultListScreenEvent.NavigateToImport,
        )
    }
}
