package io.github.devapro.droid.tags.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import io.github.devapro.droid.tags.mapper.TagsMapper
import io.github.devapro.droid.tags.model.TagsScreenAction
import io.github.devapro.droid.tags.model.TagsScreenEvent
import io.github.devapro.droid.tags.model.TagsScreenState

class OnRefreshReducer(
    private val runtimeRepository: VaultRuntimeRepository,
    private val tagsMapper: TagsMapper
) : Reducer<TagsScreenAction.OnRefresh, TagsScreenState, TagsScreenAction, TagsScreenEvent> {

    override val actionClass = TagsScreenAction.OnRefresh::class

    override suspend fun reduce(
        action: TagsScreenAction.OnRefresh,
        getState: () -> TagsScreenState
    ): Reducer.Result<TagsScreenState, TagsScreenAction.OnRefresh, TagsScreenEvent?> {
        val currentState = getState()

        return if (currentState is TagsScreenState.Success) {
            val vault = runtimeRepository.getVault()
            val tags = tagsMapper.map(vault.items)

            Reducer.Result(
                state = currentState.copy(
                    tags = tags,
                    filteredTags = if (currentState.hasSearchQuery) {
                        tags.filter { tag ->
                            tag.name.contains(currentState.searchQuery, ignoreCase = true)
                        }
                    } else {
                        tags
                    },
                    isRefreshing = false
                ),
                action = null,
                event = null
            )
        } else {
            Reducer.Result(
                state = currentState,
                action = null,
                event = null
            )
        }
    }
} 