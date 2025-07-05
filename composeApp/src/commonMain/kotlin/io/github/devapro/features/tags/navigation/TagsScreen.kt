package io.github.devapro.features.tags.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.features.tags.TagsScreenRoot

object TagsScreen : Screen {

    @Composable
    override fun Content() {
        TagsScreenRoot()
    }
} 