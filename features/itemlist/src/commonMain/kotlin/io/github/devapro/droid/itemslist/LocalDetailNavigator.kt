package io.github.devapro.droid.itemslist

import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.voyager.navigator.Navigator

/**
 * CompositionLocal for accessing the detail navigator in master-detail layouts.
 * Will be null on small screens where single-pane navigation is used.
 *
 * This should be provided at the root level of screens that support master-detail layout,
 * and can be accessed by any child composable to determine whether to use detail-pane
 * navigation (on large screens) or full-screen navigation (on small screens).
 */
internal val LocalDetailNavigator = staticCompositionLocalOf<Navigator?> { null }
