package io.github.devapro.features.tags.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.features.tags.model.TagItemType
import io.github.devapro.features.tags.model.TagsScreenAction
import io.github.devapro.features.tags.model.TagsScreenEvent
import io.github.devapro.features.tags.model.TagsScreenState

class OnSearchChangedReducer
    :
    Reducer<TagsScreenAction.OnSearchChanged, TagsScreenState, TagsScreenAction, TagsScreenEvent> {

    override val actionClass = TagsScreenAction.OnSearchChanged::class

    override suspend fun reduce(
        action: TagsScreenAction.OnSearchChanged,
        getState: () -> TagsScreenState
    ): Reducer.Result<TagsScreenState, TagsScreenAction.OnSearchChanged, TagsScreenEvent?> {
        val currentState = getState()

        return if (currentState is TagsScreenState.Success) {
            val filteredTags = if (action.query.isBlank()) {
                currentState.tags
            } else {
                currentState.tags.filter { tag ->
                    tag.name.contains(
                        action.query,
                        ignoreCase = true
                    ) && tag.type == TagItemType.NORMAL
                }
            }

            Reducer.Result(
                state = currentState.copy(
                    searchQuery = action.query,
                    filteredTags = filteredTags,
                    hasSearchQuery = action.query.isNotBlank()
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