package io.github.devapro.features.tags

import io.github.devapro.droid.core.mvi.MviViewModel
import io.github.devapro.features.tags.model.TagsScreenAction
import io.github.devapro.features.tags.model.TagsScreenEvent
import io.github.devapro.features.tags.model.TagsScreenState

class TagsScreenViewModel(
    actionProcessor: TagsScreenActionProcessor
) : MviViewModel<TagsScreenState, TagsScreenAction, TagsScreenEvent>(
    actionProcessor = actionProcessor
) 