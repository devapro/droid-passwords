package io.github.devapro.features.settings

import io.github.devapro.features.settings.factory.SettingsScreenInitStateFactory
import io.github.devapro.features.settings.model.SettingsScreenState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Integration tests for SettingsScreenActionProcessor and MVI components.
 * Tests the basic structure and creation of MVI components.
 */
class SettingsScreenActionProcessorIntegrationTest {

    @Test
    fun testInitStateFactory() {
        val factory = SettingsScreenInitStateFactory()
        val initialState = factory.createInitState()

        assertEquals(SettingsScreenState.Loading, initialState)
        assertTrue(initialState is SettingsScreenState.Loading)
    }

    @Test
    fun testActionProcessorClassExists() {
        // Verify the ActionProcessor class exists and can be referenced
        val actionProcessorClass = SettingsScreenActionProcessor::class
        assertEquals("SettingsScreenActionProcessor", actionProcessorClass.simpleName)
    }

    @Test
    fun testMVIComponentsExist() {
        // Test that all MVI components can be referenced
        assertTrue(SettingsScreenState.Loading is SettingsScreenState.Loading)

        val errorState = SettingsScreenState.Error("Test error")
        assertTrue(errorState is SettingsScreenState.Error)
        assertEquals("Test error", errorState.message)

        val successState = SettingsScreenState.Success(
            lockInterval = io.github.devapro.model.LockInterval.NEVER,
            themeMode = io.github.devapro.model.ThemeMode.LIGHT,
            vaultFilePath = "/test/path"
        )
        assertTrue(successState is SettingsScreenState.Success)
    }

    @Test
    fun testActionTypes() {
        // Test that action types exist and can be created
        val initAction = io.github.devapro.features.settings.model.SettingsScreenAction.InitScreen
        assertTrue(initAction is io.github.devapro.features.settings.model.SettingsScreenAction.InitScreen)

        val backAction =
            io.github.devapro.features.settings.model.SettingsScreenAction.OnBackClicked
        assertTrue(backAction is io.github.devapro.features.settings.model.SettingsScreenAction.OnBackClicked)

        val lockIntervalAction =
            io.github.devapro.features.settings.model.SettingsScreenAction.OnLockIntervalChanged(
                io.github.devapro.model.LockInterval.FIFTEEN_MINUTES
            )
        assertTrue(lockIntervalAction is io.github.devapro.features.settings.model.SettingsScreenAction.OnLockIntervalChanged)
    }

    @Test
    fun testEventTypes() {
        // Test that event types exist and can be created
        val navigateBackEvent =
            io.github.devapro.features.settings.model.SettingsScreenEvent.NavigateBack
        assertTrue(navigateBackEvent is io.github.devapro.features.settings.model.SettingsScreenEvent.NavigateBack)

        val showErrorEvent =
            io.github.devapro.features.settings.model.SettingsScreenEvent.ShowError("Test error")
        assertTrue(showErrorEvent is io.github.devapro.features.settings.model.SettingsScreenEvent.ShowError)
        assertEquals("Test error", showErrorEvent.message)

        val showSuccessEvent =
            io.github.devapro.features.settings.model.SettingsScreenEvent.ShowSuccess("Test success")
        assertTrue(showSuccessEvent is io.github.devapro.features.settings.model.SettingsScreenEvent.ShowSuccess)
        assertEquals("Test success", showSuccessEvent.message)
    }

    @Test
    fun testStateTransitions() {
        // Test basic state transition concepts
        val loadingState = SettingsScreenState.Loading
        val errorState = SettingsScreenState.Error("Failed to load")
        val successState = SettingsScreenState.Success(
            lockInterval = io.github.devapro.model.LockInterval.THIRTY_MINUTES,
            themeMode = io.github.devapro.model.ThemeMode.SYSTEM,
            vaultFilePath = "default_cache_directory"
        )

        // Test state types
        assertTrue(loadingState is SettingsScreenState.Loading)
        assertTrue(errorState is SettingsScreenState.Error)
        assertTrue(successState is SettingsScreenState.Success)

        // Test state properties
        assertEquals("Failed to load", errorState.message)
        assertEquals(io.github.devapro.model.LockInterval.THIRTY_MINUTES, successState.lockInterval)
        assertEquals(io.github.devapro.model.ThemeMode.SYSTEM, successState.themeMode)
        assertEquals("default_cache_directory", successState.vaultFilePath)
    }

    @Test
    fun testDialogStates() {
        // Test dialog state management
        val initialState = SettingsScreenState.Success(
            lockInterval = io.github.devapro.model.LockInterval.NEVER,
            themeMode = io.github.devapro.model.ThemeMode.LIGHT,
            vaultFilePath = "/test/path"
        )

        // Test default dialog states
        assertEquals(false, initialState.isChangePasswordDialogVisible)
        assertEquals(false, initialState.isFilePathDialogVisible)
        assertEquals(false, initialState.isChangingPassword)
        assertEquals(false, initialState.isChangingFilePath)
        assertEquals(null, initialState.passwordChangeError)
        assertEquals(null, initialState.filePathChangeError)

        // Test dialog state changes
        val stateWithPasswordDialog = initialState.copy(isChangePasswordDialogVisible = true)
        assertEquals(true, stateWithPasswordDialog.isChangePasswordDialogVisible)

        val stateWithFilePathDialog = initialState.copy(isFilePathDialogVisible = true)
        assertEquals(true, stateWithFilePathDialog.isFilePathDialogVisible)

        val stateWithError = initialState.copy(passwordChangeError = "Test error")
        assertEquals("Test error", stateWithError.passwordChangeError)
    }
}