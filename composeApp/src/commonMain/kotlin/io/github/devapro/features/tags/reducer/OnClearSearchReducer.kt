package io.github.devapro.features.tags.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.tags.model.TagsScreenAction
import io.github.devapro.features.tags.model.TagsScreenEvent
import io.github.devapro.features.tags.model.TagsScreenState

class OnClearSearchReducer
    : Reducer<TagsScreenAction.OnClearSearch, TagsScreenState, TagsScreenAction, TagsScreenEvent> {

    override val actionClass = TagsScreenAction.OnClearSearch::class

    override suspend fun reduce(
        action: TagsScreenAction.OnClearSearch,
        getState: () -> TagsScreenState
    ): Reducer.Result<TagsScreenState, TagsScreenAction.OnClearSearch, TagsScreenEvent?> {
        val currentState = getState()

        return if (currentState is TagsScreenState.Success) {
            Reducer.Result(
                state = currentState.copy(
                    searchQuery = "",
                    filteredTags = currentState.tags,
                    hasSearchQuery = false
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