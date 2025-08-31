package io.github.devapro.droid.tags.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.tags.model.TagsScreenAction
import io.github.devapro.droid.tags.model.TagsScreenEvent
import io.github.devapro.droid.tags.model.TagsScreenState

class OnSettingsClickedReducer
    :
    Reducer<TagsScreenAction.OnSettingsClicked, TagsScreenState, TagsScreenAction, TagsScreenEvent> {

    override val actionClass = TagsScreenAction.OnSettingsClicked::class

    override suspend fun reduce(
        action: TagsScreenAction.OnSettingsClicked,
        getState: () -> TagsScreenState
    ): Reducer.Result<TagsScreenState, TagsScreenAction.OnSettingsClicked, TagsScreenEvent?> {
        return Reducer.Result(
            state = getState(),
            action = null,
            event = TagsScreenEvent.NavigateToSettings
        )
    }
} 