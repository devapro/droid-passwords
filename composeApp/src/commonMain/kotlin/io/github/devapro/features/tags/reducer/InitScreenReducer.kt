package io.github.devapro.features.tags.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.data.vault.VaultRuntimeRepository
import io.github.devapro.features.tags.mapper.TagsMapper
import io.github.devapro.features.tags.model.TagsScreenAction
import io.github.devapro.features.tags.model.TagsScreenEvent
import io.github.devapro.features.tags.model.TagsScreenState

class InitScreenReducer(
    private val runtimeRepository: VaultRuntimeRepository,
    private val tagsMapper: TagsMapper
) : Reducer<TagsScreenAction.InitScreen, TagsScreenState, TagsScreenAction, TagsScreenEvent> {

    override val actionClass = TagsScreenAction.InitScreen::class

    override suspend fun reduce(
        action: TagsScreenAction.InitScreen,
        getState: () -> TagsScreenState
    ): Reducer.Result<TagsScreenState, TagsScreenAction.InitScreen, TagsScreenEvent?> {
        val vault = runtimeRepository.getVault()
        val tags = tagsMapper.map(vault.items)
        return Reducer.Result(
            state = TagsScreenState.Success(
                tags = tags,
                filteredTags = tags,
                searchQuery = "",
                isLoading = false,
                isRefreshing = false,
                hasSearchQuery = false
            ),
            action = null,
            event = null
        )
    }
} 