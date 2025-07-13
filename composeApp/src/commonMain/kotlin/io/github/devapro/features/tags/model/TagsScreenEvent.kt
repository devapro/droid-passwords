package io.github.devapro.features.tags.model

sealed interface TagsScreenEvent {

    data class NavigateToTagDetail(val tag: TagItemModel) : TagsScreenEvent

    data object NavigateBack : TagsScreenEvent

    data object NavigateToAddPassword : TagsScreenEvent

    data object NavigateToExport : TagsScreenEvent

    data object NavigateToSettings : TagsScreenEvent
} 