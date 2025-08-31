package io.github.devapro.droid.tags.navigation

import androidx.compose.runtime.Composable
import io.github.devapro.droid.tags.TagsScreenFactory
import io.github.devapro.droid.tags.TagsScreenRoot

class TagsScreenFactoryImpl: TagsScreenFactory {

    @Composable
    override fun CreateTagsScreen() {
        TagsScreenRoot()
    }
}