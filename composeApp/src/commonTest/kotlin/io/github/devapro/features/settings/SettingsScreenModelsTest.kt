package io.github.devapro.features.settings

import io.github.devapro.data.model.LockInterval
import io.github.devapro.data.model.ThemeMode
import io.github.devapro.features.settings.model.SettingsScreenAction
import io.github.devapro.features.settings.model.SettingsScreenEvent
import io.github.devapro.features.settings.model.SettingsScreenState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Unit tests for Settings Screen MVI models.
 * Tests the state, action, and event models for the settings screen.
 */
class SettingsScreenModelsTest {

    @Test
    fun testSettingsScreenStateTypes() {
        // Test Loading state
        val loadingState = SettingsScreenState.Loading
        assertTrue(loadingState is SettingsScreenState.Loading)

        // Test Error state
        val errorState = SettingsScreenState.Error("Test error")
        assertTrue(errorState is SettingsScreenState.Error)
        assertEquals("Test error", errorState.message)

        // Test Success state with default values
        val successState = SettingsScreenState.Success(
            lockInterval = LockInterval.NEVER,
            themeMode = ThemeMode.LIGHT,
            vaultFilePath = "/test/path"
        )
        assertTrue(successState is SettingsScreenState.Success)
        assertEquals(LockInterval.NEVER, successState.lockInterval)
        assertEquals(ThemeMode.LIGHT, successState.themeMode)
        assertEquals("/test/path", successState.vaultFilePath)
        assertEquals(false, successState.isChangePasswordDialogVisible)
        assertEquals(false, successState.isFilePathDialogVisible)
        assertEquals(false, successState.isChangingPassword)
        assertEquals(false, successState.isChangingFilePath)
        assertNull(successState.passwordChangeError)
        assertNull(successState.filePathChangeError)
    }

    @Test
    fun testSettingsScreenSuccessStateWithCustomValues() {
        // Test Success state with custom values
        val successState = SettingsScreenState.Success(
            lockInterval = LockInterval.FIFTEEN_MINUTES,
            themeMode = ThemeMode.DARK,
            vaultFilePath = "/custom/path",
            isChangePasswordDialogVisible = true,
            isFilePathDialogVisible = true,
            isChangingPassword = true,
            isChangingFilePath = true,
            passwordChangeError = "Password error",
            filePathChangeError = "Path error"
        )

        assertEquals(LockInterval.FIFTEEN_MINUTES, successState.lockInterval)
        assertEquals(ThemeMode.DARK, successState.themeMode)
        assertEquals("/custom/path", successState.vaultFilePath)
        assertEquals(true, successState.isChangePasswordDialogVisible)
        assertEquals(true, successState.isFilePathDialogVisible)
        assertEquals(true, successState.isChangingPassword)
        assertEquals(true, successState.isChangingFilePath)
        assertEquals("Password error", successState.passwordChangeError)
        assertEquals("Path error", successState.filePathChangeError)
    }

    @Test
    fun testSettingsScreenActionTypes() {
        // Test InitScreen action
        val initAction = SettingsScreenAction.InitScreen
        assertTrue(initAction is SettingsScreenAction.InitScreen)

        // Test OnBackClicked action
        val backAction = SettingsScreenAction.OnBackClicked
        assertTrue(backAction is SettingsScreenAction.OnBackClicked)

        // Test OnChangePasswordClicked action
        val changePasswordAction = SettingsScreenAction.OnChangePasswordClicked
        assertTrue(changePasswordAction is SettingsScreenAction.OnChangePasswordClicked)

        // Test OnFilePathClicked action
        val filePathAction = SettingsScreenAction.OnFilePathClicked
        assertTrue(filePathAction is SettingsScreenAction.OnFilePathClicked)

        // Test OnLockIntervalChanged action
        val lockIntervalAction =
            SettingsScreenAction.OnLockIntervalChanged(LockInterval.FIFTEEN_MINUTES)
        assertTrue(lockIntervalAction is SettingsScreenAction.OnLockIntervalChanged)
        assertEquals(LockInterval.FIFTEEN_MINUTES, lockIntervalAction.interval)

        // Test OnThemeModeChanged action
        val themeModeAction = SettingsScreenAction.OnThemeModeChanged(ThemeMode.DARK)
        assertTrue(themeModeAction is SettingsScreenAction.OnThemeModeChanged)
        assertEquals(ThemeMode.DARK, themeModeAction.mode)

        // Test OnPasswordChangeSubmitted action
        val passwordSubmitAction =
            SettingsScreenAction.OnPasswordChangeSubmitted("old", "new", "new")
        assertTrue(passwordSubmitAction is SettingsScreenAction.OnPasswordChangeSubmitted)
        assertEquals("old", passwordSubmitAction.currentPassword)
        assertEquals("new", passwordSubmitAction.newPassword)
        assertEquals("new", passwordSubmitAction.confirmPassword)

        // Test OnFilePathSelected action
        val filePathSelectedAction = SettingsScreenAction.OnFilePathSelected("/new/path")
        assertTrue(filePathSelectedAction is SettingsScreenAction.OnFilePathSelected)
        assertEquals("/new/path", filePathSelectedAction.path)

        // Test OnDismissChangePasswordDialog action
        val dismissPasswordDialogAction = SettingsScreenAction.OnDismissChangePasswordDialog
        assertTrue(dismissPasswordDialogAction is SettingsScreenAction.OnDismissChangePasswordDialog)

        // Test OnDismissFilePathDialog action
        val dismissFilePathDialogAction = SettingsScreenAction.OnDismissFilePathDialog
        assertTrue(dismissFilePathDialogAction is SettingsScreenAction.OnDismissFilePathDialog)
    }

    @Test
    fun testSettingsScreenEventTypes() {
        // Test NavigateBack event
        val navigateBackEvent = SettingsScreenEvent.NavigateBack
        assertTrue(navigateBackEvent is SettingsScreenEvent.NavigateBack)

        // Test ShowError event
        val showErrorEvent = SettingsScreenEvent.ShowError("Test error")
        assertTrue(showErrorEvent is SettingsScreenEvent.ShowError)
        assertEquals("Test error", showErrorEvent.message)

        // Test ShowSuccess event
        val showSuccessEvent = SettingsScreenEvent.ShowSuccess("Test success")
        assertTrue(showSuccessEvent is SettingsScreenEvent.ShowSuccess)
        assertEquals("Test success", showSuccessEvent.message)

        // Test ShowFilePathPicker event
        val showFilePathPickerEvent = SettingsScreenEvent.ShowFilePathPicker
        assertTrue(showFilePathPickerEvent is SettingsScreenEvent.ShowFilePathPicker)
    }

    @Test
    fun testPasswordChangeActionValidation() {
        // Test password change action with different scenarios
        val validAction = SettingsScreenAction.OnPasswordChangeSubmitted("old", "new", "new")
        assertEquals("old", validAction.currentPassword)
        assertEquals("new", validAction.newPassword)
        assertEquals("new", validAction.confirmPassword)
        assertTrue(validAction.newPassword == validAction.confirmPassword)

        val emptyCurrentAction = SettingsScreenAction.OnPasswordChangeSubmitted("", "new", "new")
        assertTrue(emptyCurrentAction.currentPassword.isEmpty())

        val emptyNewAction = SettingsScreenAction.OnPasswordChangeSubmitted("old", "", "")
        assertTrue(emptyNewAction.newPassword.isEmpty())

        val mismatchedAction = SettingsScreenAction.OnPasswordChangeSubmitted("old", "new1", "new2")
        assertTrue(mismatchedAction.newPassword != mismatchedAction.confirmPassword)
    }

    @Test
    fun testFilePathActionValidation() {
        // Test file path action with different scenarios
        val validPathAction = SettingsScreenAction.OnFilePathSelected("/valid/path")
        assertEquals("/valid/path", validPathAction.path)
        assertTrue(validPathAction.path.isNotEmpty())

        val emptyPathAction = SettingsScreenAction.OnFilePathSelected("")
        assertTrue(emptyPathAction.path.isEmpty())

        val relativePathAction = SettingsScreenAction.OnFilePathSelected("/some/../path")
        assertTrue(relativePathAction.path.contains(".."))

        val windowsPathAction = SettingsScreenAction.OnFilePathSelected("C:\\Users\\Test")
        assertEquals("C:\\Users\\Test", windowsPathAction.path)
    }

    @Test
    fun testStateTransitions() {
        // Test typical state transitions
        val initialState = SettingsScreenState.Loading
        assertTrue(initialState is SettingsScreenState.Loading)

        // Transition to success
        val successState = SettingsScreenState.Success(
            lockInterval = LockInterval.THIRTY_MINUTES,
            themeMode = ThemeMode.SYSTEM,
            vaultFilePath = "default_cache_directory"
        )
        assertTrue(successState is SettingsScreenState.Success)

        // Transition to error
        val errorState = SettingsScreenState.Error("Failed to load settings")
        assertTrue(errorState is SettingsScreenState.Error)

        // Test state copying (simulating reducer behavior)
        val updatedState = successState.copy(
            lockInterval = LockInterval.NEVER,
            isChangePasswordDialogVisible = true
        )
        assertEquals(LockInterval.NEVER, updatedState.lockInterval)
        assertEquals(true, updatedState.isChangePasswordDialogVisible)
        assertEquals(ThemeMode.SYSTEM, updatedState.themeMode) // Should remain unchanged
    }
}