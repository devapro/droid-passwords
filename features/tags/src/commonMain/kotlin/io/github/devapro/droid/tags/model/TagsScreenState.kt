package io.github.devapro.droid.tags.model

sealed interface TagsScreenState {
    data object Loading : TagsScreenState

    data class Success(
        val tags: List<TagItemModel>,
        val filteredTags: List<TagItemModel>,
        val searchQuery: String,
        val isLoading: Boolean,
        val isRefreshing: Boolean,
        val hasSearchQuery: Boolean
    ) : TagsScreenState
} 