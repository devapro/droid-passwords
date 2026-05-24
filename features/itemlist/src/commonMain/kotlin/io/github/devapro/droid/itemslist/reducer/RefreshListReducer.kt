package io.github.devapro.droid.itemslist.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import io.github.devapro.droid.itemlist.PasswordTagFilterType
import io.github.devapro.droid.itemslist.applyFilter
import io.github.devapro.droid.itemslist.applySort
import io.github.devapro.droid.itemslist.mapper.VaultItemMapper
import io.github.devapro.droid.itemslist.model.PasswordListScreenAction
import io.github.devapro.droid.itemslist.model.PasswordListScreenEvent
import io.github.devapro.droid.itemslist.model.PasswordListScreenState

class RefreshListReducer(
    private val runtimeRepository: VaultRuntimeRepository,
    private val vaultItemMapper: VaultItemMapper
) : Reducer<PasswordListScreenAction.RefreshList, PasswordListScreenState, PasswordListScreenAction, PasswordListScreenEvent> {

    override val actionClass = PasswordListScreenAction.RefreshList::class

    override suspend fun reduce(
        action: PasswordListScreenAction.RefreshList,
        getState: () -> PasswordListScreenState
    ): Reducer.Result<PasswordListScreenState, PasswordListScreenAction.RefreshList, PasswordListScreenEvent?> {
        val currentState = getState()
        if (currentState !is PasswordListScreenState.Success) {
            return Reducer.Result(state = currentState)
        }
        if (runtimeRepository.getActiveVaultId() == null) {
            return Reducer.Result(state = currentState)
        }
        val vault = runtimeRepository.getVault()
        val filteredByTag = when (currentState.tagFilterType) {
            PasswordTagFilterType.ALL -> vault.items
            PasswordTagFilterType.NO_TAG -> vault.items.filter { it.tags.isEmpty() }
            PasswordTagFilterType.NORMAL -> vault.items.filter {
                it.tags.any { tag -> tag.id == currentState.selectedTag?.id }
            }
        }
        val sortedAll = vaultItemMapper.map(filteredByTag).applySort(currentState.sortOrder)
        val filtered = sortedAll.applyFilter(currentState.searchQuery)
        return Reducer.Result(
            state = currentState.copy(
                passwords = sortedAll,
                filteredPasswords = filtered
            )
        )
    }
}
