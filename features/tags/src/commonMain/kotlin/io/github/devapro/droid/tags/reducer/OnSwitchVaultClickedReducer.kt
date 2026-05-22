package io.github.devapro.droid.tags.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.tags.model.TagsScreenAction
import io.github.devapro.droid.tags.model.TagsScreenEvent
import io.github.devapro.droid.tags.model.TagsScreenState

class OnSwitchVaultClickedReducer
    : Reducer<TagsScreenAction.OnSwitchVaultClicked, TagsScreenState, TagsScreenAction, TagsScreenEvent> {

    override val actionClass = TagsScreenAction.OnSwitchVaultClicked::class

    override suspend fun reduce(
        action: TagsScreenAction.OnSwitchVaultClicked,
        getState: () -> TagsScreenState
    ): Reducer.Result<TagsScreenState, TagsScreenAction.OnSwitchVaultClicked, TagsScreenEvent?> {
        return Reducer.Result(
            state = getState(),
            action = null,
            event = TagsScreenEvent.NavigateToVaultList,
        )
    }
}
