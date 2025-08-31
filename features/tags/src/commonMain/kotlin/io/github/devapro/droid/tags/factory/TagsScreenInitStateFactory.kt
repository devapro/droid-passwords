package io.github.devapro.droid.tags.factory

import io.github.devapro.droid.core.mvi.InitStateFactory
import io.github.devapro.droid.tags.model.TagsScreenState

class TagsScreenInitStateFactory : InitStateFactory<TagsScreenState> {

    override fun createInitState(): TagsScreenState {
        return TagsScreenState.Loading
    }
} 