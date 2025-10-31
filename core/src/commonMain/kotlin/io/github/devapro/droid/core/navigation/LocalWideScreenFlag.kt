package io.github.devapro.droid.core.navigation

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * CompositionLocal for detecting wide screens (≥600dp).
 *
 * This should be provided at the app level using BoxWithConstraints to detect
 * the actual screen width, and then can be consumed by any composable to determine
 * whether to use wide-screen layouts (two columns, larger spacing, etc.).
 *
 * Default value is false (narrow screen) as a safe fallback.
 */
val LocalWideScreenFlag = staticCompositionLocalOf { false }