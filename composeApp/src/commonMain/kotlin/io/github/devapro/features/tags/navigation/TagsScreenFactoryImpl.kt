package io.github.devapro.features.tags.navigation

import androidx.compose.runtime.Composable
import io.github.devapro.droid.tags.TagsScreenFactory
import io.github.devapro.features.tags.TagsScreenRoot

class TagsScreenFactoryImpl: TagsScreenFactory {

    @Composable
    override fun CreateTagsScreen() {
        TagsScreenRoot()
    }
}