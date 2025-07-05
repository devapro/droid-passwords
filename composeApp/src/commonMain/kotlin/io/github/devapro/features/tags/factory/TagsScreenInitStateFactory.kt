package io.github.devapro.features.tags.factory

import io.github.devapro.core.mvi.InitStateFactory
import io.github.devapro.features.tags.model.TagsScreenState

class TagsScreenInitStateFactory : InitStateFactory<TagsScreenState> {

    override fun createInitState(): TagsScreenState {
        return TagsScreenState.Loading
    }
} 