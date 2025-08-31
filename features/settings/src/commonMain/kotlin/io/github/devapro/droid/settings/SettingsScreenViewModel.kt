package io.github.devapro.droid.settings

import io.github.devapro.droid.core.mvi.MviViewModel
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

/**
 * ViewModel for the Settings Screen that manages state and coordinates user actions.
 *
 * This ViewModel extends MviViewModel and provides the interface between the UI layer
 * and the business logic layer. It:
 * - Exposes state and event flows for the UI to observe
 * - Processes user actions through the ActionProcessor
 * - Manages the lifecycle of settings screen operations
 *
 * The ViewModel integrates with the SettingsScreenActionProcessor to handle:
 * - Screen initialization and settings loading
 * - Password change operations with validation
 * - File path selection and validation
 * - Lock interval configuration changes
 * - Theme mode updates
 * - Dialog state management
 * - Navigation events
 *
 * Requirements covered:
 * - 5.1: Provides state management for settings screen
 * - 5.2: Handles navigation events and user interactions
 * - 5.3: Manages settings changes and persistence
 * - 5.4: Coordinates with ActionProcessor for business logic
 */
class SettingsScreenViewModel(
    actionProcessor: SettingsScreenActionProcessor
) : MviViewModel<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent>(
    actionProcessor = actionProcessor
)