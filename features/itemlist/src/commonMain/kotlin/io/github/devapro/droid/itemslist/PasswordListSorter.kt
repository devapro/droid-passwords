package io.github.devapro.droid.itemslist

import io.github.devapro.droid.data.model.ItemModel
import io.github.devapro.droid.itemslist.model.SortOrder

internal fun List<ItemModel>.applySort(order: SortOrder): List<ItemModel> {
    return when (order) {
        SortOrder.NAME_ASC -> sortedBy { it.title.lowercase() }
        SortOrder.NAME_DESC -> sortedByDescending { it.title.lowercase() }
        SortOrder.CREATED_DESC -> sortedByDescending { it.createdAt ?: 0L }
        SortOrder.CREATED_ASC -> sortedBy { it.createdAt ?: Long.MAX_VALUE }
        SortOrder.UPDATED_DESC -> sortedByDescending { it.updatedAt ?: it.createdAt ?: 0L }
        SortOrder.LAST_USED_DESC -> sortedByDescending { it.lastUsedAt ?: 0L }
    }
}

internal fun List<ItemModel>.applyFilter(query: String): List<ItemModel> {
    if (query.isBlank()) return this
    return filter { item ->
        item.title.contains(query, ignoreCase = true) ||
            item.description.contains(query, ignoreCase = true) ||
            item.url.contains(query, ignoreCase = true) ||
            item.username.contains(query, ignoreCase = true)
    }
}
