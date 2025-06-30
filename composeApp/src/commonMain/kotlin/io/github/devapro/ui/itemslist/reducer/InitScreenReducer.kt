package io.github.devapro.ui.itemslist.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.data.vault.VaultRuntimeRepository
import io.github.devapro.ui.itemslist.mapper.VaultItemMapper
import io.github.devapro.ui.itemslist.model.PasswordListScreenAction
import io.github.devapro.ui.itemslist.model.PasswordListScreenEvent
import io.github.devapro.ui.itemslist.model.PasswordListScreenState

class InitScreenReducer(
    private val runtimeRepository: VaultRuntimeRepository,
    private val vaultItemMapper: VaultItemMapper
) : Reducer<PasswordListScreenAction.InitScreen, PasswordListScreenState, PasswordListScreenAction, PasswordListScreenEvent> {

    override val actionClass = PasswordListScreenAction.InitScreen::class

    override suspend fun reduce(
        action: PasswordListScreenAction.InitScreen,
        getState: () -> PasswordListScreenState
    ): Reducer.Result<PasswordListScreenState, PasswordListScreenAction.InitScreen, PasswordListScreenEvent?> {
        val vault = runtimeRepository.getVault()
        val items = vaultItemMapper.map(vault.items)
        return Reducer.Result(
            state = PasswordListScreenState.Success(
                passwords = items,
                filteredPasswords = items,
                searchQuery = "",
                isLoading = false,
                isRefreshing = false,
                isEmpty = true,
                hasSearchQuery = false
            ),
            action = null,
            event = PasswordListScreenEvent.RefreshPasswordList
        )
    }
} 