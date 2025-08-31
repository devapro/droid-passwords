package io.github.devapro.droid.tags.model

sealed interface TagsScreenAction {
    data object InitScreen : TagsScreenAction

    data class OnSearchChanged(val query: String) : TagsScreenAction

    data class OnTagClicked(val tag: TagItemModel) : TagsScreenAction

    data object OnRefresh : TagsScreenAction

    data object OnClearSearch : TagsScreenAction

    data object OnBackClicked : TagsScreenAction

    data object OnExportClicked : TagsScreenAction

    data object OnSettingsClicked : TagsScreenAction

    data object OnAddPasswordClicked : TagsScreenAction
} 