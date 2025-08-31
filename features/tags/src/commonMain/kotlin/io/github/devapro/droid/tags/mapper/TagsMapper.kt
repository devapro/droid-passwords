package io.github.devapro.droid.tags.mapper

import io.github.devapro.droid.data.vault.VaultItemModel
import io.github.devapro.droid.tags.model.TagItemModel
import io.github.devapro.droid.tags.model.TagItemType

class TagsMapper {

    fun map(vaultItems: List<VaultItemModel>): List<TagItemModel> {
        // Count occurrences of each tag
        val tagCounts = mutableMapOf<String, Int>()
        var itemsWithoutTags = 0

        vaultItems.forEach { item ->
            if (item.tags.isEmpty()) {
                itemsWithoutTags++
            } else {
                item.tags.forEach { tag ->
                    tagCounts[tag.id] = tagCounts.getOrDefault(tag.id, 0) + 1
                }
            }
        }

        // Convert to TagModel list
        val tags = mutableListOf<TagItemModel>()

        // Get all unique tags from all items
        val allTags = vaultItems.flatMap { it.tags }.distinctBy { it.id }

        // Add tags with counts
        allTags.forEach { vaultTag ->
            val count = tagCounts[vaultTag.id] ?: 0
            tags.add(
                TagItemModel(
                    id = vaultTag.id,
                    name = vaultTag.title,
                    count = count,
                    type = TagItemType.NORMAL,
                    tag = vaultTag
                )
            )
        }

        // Add "No tags" entry if there are items without tags
        if (itemsWithoutTags > 0) {
            tags.add(
                TagItemModel(
                    id = "no_tags",
                    name = "No tags",
                    count = itemsWithoutTags,
                    type = TagItemType.NO_TAGS,
                    tag = null
                )
            )
        }

        tags.add(
            TagItemModel(
                id = "all",
                name = "All",
                count = vaultItems.size,
                type = TagItemType.ALL,
                tag = null
            )
        )

        // Sort by name
        return tags.sortedBy { it.name }
    }
} 