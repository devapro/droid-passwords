package io.github.devapro.droid.settings

import io.github.devapro.droid.core.mvi.ActionProcessor
import io.github.devapro.droid.core.mvi.CoroutineContextProvider
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.settings.factory.SettingsScreenInitStateFactory
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

/**
 * ActionProcessor for the Settings Screen that coordinates all user actions and state updates.
 *
 * This processor integrates all the settings screen reducers and manages the flow of actions,
 * state updates, and events. It handles:
 * - Screen initialization and loading settings
 * - Password change operations with validation and error handling
 * - File path selection and validation
 * - Lock interval configuration
 * - Theme mode changes
 * - Dialog state management
 * - Navigation events
 *
 * The processor uses the IO dispatcher for async operations like repository calls
 * and ensures proper error handling and state management throughout the settings flow.
 */
class SettingsScreenActionProcessor(
    reducers: Set<Reducer<SettingsScreenAction, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent>>,
    initStateFactory: SettingsScreenInitStateFactory,
    coroutineContextProvider: CoroutineContextProvider
) : ActionProcessor<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent>(
    reducers = reducers,
    initStateFactory = initStateFactory,
    coroutineDispatcher = coroutineContextProvider.io
)