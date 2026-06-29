package io.github.devapro.droid.itemslist.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import io.github.devapro.droid.itemlist.PasswordTagFilterType
import io.github.devapro.droid.itemslist.applySort
import io.github.devapro.droid.itemslist.mapper.VaultItemMapper
import io.github.devapro.droid.itemslist.model.PasswordListScreenAction
import io.github.devapro.droid.itemslist.model.PasswordListScreenEvent
import io.github.devapro.droid.itemslist.model.PasswordListScreenState
import io.github.devapro.droid.itemslist.model.SortOrder

class InitScreenReducer(
    private val runtimeRepository: VaultRuntimeRepository,
    private val vaultItemMapper: VaultItemMapper
) : Reducer<PasswordListScreenAction.InitScreen, PasswordListScreenState, PasswordListScreenAction, PasswordListScreenEvent> {

    override val actionClass = PasswordListScreenAction.InitScreen::class

    override suspend fun reduce(
        action: PasswordListScreenAction.InitScreen,
        getState: () -> PasswordListScreenState
    ): Reducer.Result<PasswordListScreenState, PasswordListScreenAction.InitScreen, PasswordListScreenEvent?> {
        val vaultItems = runtimeRepository.getActiveVaultOrNull()?.items.orEmpty()
        val filteredItems = when (action.tagFilterType) {
            PasswordTagFilterType.ALL -> vaultItems
            PasswordTagFilterType.NO_TAG -> vaultItems.filter { it.tags.isEmpty() }
            PasswordTagFilterType.NORMAL -> vaultItems.filter { it.tags.any { tag -> tag.id == action.tag?.id } }
        }
        val items = vaultItemMapper.map(filteredItems)
        val initialOrder = SortOrder.NAME_ASC
        val sortedItems = items.applySort(initialOrder)
        return Reducer.Result(
            state = PasswordListScreenState.Success(
                passwords = sortedItems,
                filteredPasswords = sortedItems,
                searchQuery = "",
                isLoading = false,
                isRefreshing = false,
                hasSearchQuery = false,
                title = when (action.tagFilterType) {
                    PasswordTagFilterType.ALL -> "All Passwords"
                    PasswordTagFilterType.NO_TAG -> "No Tag Passwords"
                    PasswordTagFilterType.NORMAL -> action.tag?.title ?: "Tagged Passwords"
                },
                tagFilterType = action.tagFilterType,
                selectedTag = action.tag,
                sortOrder = initialOrder
            ),
            action = null,
            event = null
        )
    }
} 