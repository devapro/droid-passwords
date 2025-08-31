package io.github.devapro.droid.tags.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.droid.tags.TagsScreenFactory
import org.koin.compose.koinInject

object TagsScreen : Screen {

    @Composable
    override fun Content() {
        val factory: TagsScreenFactory = koinInject()
        factory.CreateTagsScreen()
    }
}