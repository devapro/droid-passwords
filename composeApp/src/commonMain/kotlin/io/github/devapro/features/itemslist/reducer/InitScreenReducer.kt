package io.github.devapro.features.itemslist.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import io.github.devapro.droid.itemlist.PasswordTagFilterType
import io.github.devapro.features.itemslist.mapper.VaultItemMapper
import io.github.devapro.features.itemslist.model.PasswordListScreenAction
import io.github.devapro.features.itemslist.model.PasswordListScreenEvent
import io.github.devapro.features.itemslist.model.PasswordListScreenState

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
        val filteredItems = when (action.tagFilterType) {
            PasswordTagFilterType.ALL -> vault.items
            PasswordTagFilterType.NO_TAG -> vault.items.filter { it.tags.isEmpty() }
            PasswordTagFilterType.NORMAL -> vault.items.filter { it.tags.any { tag -> tag.id == action.tag?.id } }
        }
        val items = vaultItemMapper.map(filteredItems)
        return Reducer.Result(
            state = PasswordListScreenState.Success(
                passwords = items,
                filteredPasswords = items,
                searchQuery = "",
                isLoading = false,
                isRefreshing = false,
                hasSearchQuery = false,
                title = when (action.tagFilterType) {
                    PasswordTagFilterType.ALL -> "All Passwords"
                    PasswordTagFilterType.NO_TAG -> "No Tag Passwords"
                    PasswordTagFilterType.NORMAL -> action.tag?.title ?: "Tagged Passwords"
                }
            ),
            action = null,
            event = null
        )
    }
} 