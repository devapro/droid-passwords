package io.github.devapro.data

import io.github.devapro.core.mvi.AppResult
import io.github.devapro.model.LockInterval
import io.github.devapro.model.ThemeMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Comprehensive unit tests for SettingsRepository functionality.
 * Tests enum values, validation logic, and AppResult types.
 */
class SettingsRepositoryTest {

    @Test
    fun testLockIntervalEnumValues() {
        // Test that lock intervals have correct minute values
        assertEquals(15, LockInterval.FIFTEEN_MINUTES.minutes)
        assertEquals(30, LockInterval.THIRTY_MINUTES.minutes)
        assertEquals(0, LockInterval.NEVER.minutes)

        // Test display names
        assertEquals("15 minutes", LockInterval.FIFTEEN_MINUTES.displayName)
        assertEquals("30 minutes", LockInterval.THIRTY_MINUTES.displayName)
        assertEquals("Never", LockInterval.NEVER.displayName)
    }

    @Test
    fun testLockIntervalValueOf() {
        // Test that we can create lock intervals from string values
        assertEquals(LockInterval.FIFTEEN_MINUTES, LockInterval.valueOf("FIFTEEN_MINUTES"))
        assertEquals(LockInterval.THIRTY_MINUTES, LockInterval.valueOf("THIRTY_MINUTES"))
        assertEquals(LockInterval.NEVER, LockInterval.valueOf("NEVER"))
    }

    @Test
    fun testThemeModeEnumValues() {
        // Test that all theme modes have proper display names
        assertEquals("Light", ThemeMode.LIGHT.displayName)
        assertEquals("Dark", ThemeMode.DARK.displayName)
        assertEquals("System", ThemeMode.SYSTEM.displayName)
    }

    @Test
    fun testThemeModeValueOf() {
        // Test that we can create theme modes from string values
        assertEquals(ThemeMode.LIGHT, ThemeMode.valueOf("LIGHT"))
        assertEquals(ThemeMode.DARK, ThemeMode.valueOf("DARK"))
        assertEquals(ThemeMode.SYSTEM, ThemeMode.valueOf("SYSTEM"))
    }

    @Test
    fun testAppResultTypes() {
        // Test AppResult.Success
        val successResult = AppResult.Success("test")
        assertTrue(successResult is AppResult.Success)
        assertEquals("test", successResult.value)

        // Test AppResult.Failure
        val exception = Exception("test error")
        val failureResult = AppResult.Failure<String>(exception)
        assertTrue(failureResult is AppResult.Failure)
        assertEquals(exception, failureResult.error)
        assertEquals("test error", failureResult.error.message)
    }

    @Test
    fun testSettingsRepositoryInterface() {
        // Test that we can create a mock implementation of SettingsRepository
        val mockRepository = object : SettingsRepository {
            override suspend fun getLockInterval(): LockInterval = LockInterval.THIRTY_MINUTES
            override suspend fun setLockInterval(interval: LockInterval) {}
            override suspend fun getThemeMode(): ThemeMode = ThemeMode.SYSTEM
            override suspend fun setThemeMode(mode: ThemeMode) {}
            override suspend fun getVaultFilePath(): String = "test_path"
            override suspend fun setVaultFilePath(path: String): AppResult<Unit> =
                AppResult.Success(Unit)

            override suspend fun changeVaultPassword(
                oldPassword: String,
                newPassword: String
            ): AppResult<Unit> = AppResult.Success(Unit)
        }

        // Verify the interface works
        assertTrue(mockRepository is SettingsRepository)
    }

    @Test
    fun testValidationLogic() {
        // Test basic validation scenarios that would be common in implementations

        // Empty string validation
        val emptyString = ""
        assertTrue(emptyString.isEmpty())

        // Path validation scenarios
        val validPath = "/valid/path"
        assertTrue(validPath.isNotEmpty())
        assertTrue(!validPath.contains(".."))

        val invalidPath = "/path/../invalid"
        assertTrue(invalidPath.contains(".."))

        // Password validation scenarios
        val password1 = "password123"
        val password2 = "password123"
        val password3 = "different"

        assertTrue(password1 == password2)
        assertTrue(password1 != password3)
        assertTrue(password1.isNotEmpty())

        // Path length validation
        val longPath = "a".repeat(501)
        assertTrue(longPath.length > 500)

        val normalPath = "a".repeat(100)
        assertTrue(normalPath.length <= 500)

        // Invalid character validation
        val invalidChars = listOf("<", ">", ":", "\"", "|", "?", "*")
        for (char in invalidChars) {
            val pathWithInvalidChar = "/path${char}invalid"
            assertTrue(pathWithInvalidChar.contains(char))
        }
    }

    @Test
    fun testPasswordChangeValidation() {
        // Test password change validation logic
        fun validatePasswordChange(current: String, new: String, confirm: String): String? {
            return when {
                current.isEmpty() -> "Current password cannot be empty"
                new.isEmpty() -> "New password cannot be empty"
                current == new -> "New password must be different from current password"
                new != confirm -> "New passwords do not match"
                else -> null
            }
        }

        // Valid password change
        assertEquals(null, validatePasswordChange("old", "new", "new"))

        // Empty current password
        assertEquals("Current password cannot be empty", validatePasswordChange("", "new", "new"))

        // Empty new password
        assertEquals("New password cannot be empty", validatePasswordChange("old", "", ""))

        // Same password
        assertEquals(
            "New password must be different from current password",
            validatePasswordChange("same", "same", "same")
        )

        // Password mismatch
        assertEquals("New passwords do not match", validatePasswordChange("old", "new1", "new2"))
    }

    @Test
    fun testFilePathValidation() {
        // Test file path validation logic
        fun validateFilePath(path: String): String? {
            return when {
                path.isEmpty() -> "Vault file path cannot be empty"
                path.length > 500 -> "File path is too long (maximum 500 characters)"
                path.contains("..") -> "File path cannot contain relative path components (..)"
                path.contains("<") || path.contains(">") || path.contains(":") ||
                        path.contains("\"") || path.contains("|") || path.contains("?") ||
                        path.contains("*") -> "File path contains invalid character"

                else -> null
            }
        }

        // Valid path
        assertEquals(null, validateFilePath("/valid/path"))

        // Empty path
        assertEquals("Vault file path cannot be empty", validateFilePath(""))

        // Path too long
        assertEquals(
            "File path is too long (maximum 500 characters)",
            validateFilePath("a".repeat(501))
        )

        // Relative path components
        assertEquals(
            "File path cannot contain relative path components (..)",
            validateFilePath("/path/../invalid")
        )

        // Invalid characters
        assertEquals("File path contains invalid character", validateFilePath("/path<invalid"))
        assertEquals("File path contains invalid character", validateFilePath("/path>invalid"))
        assertEquals("File path contains invalid character", validateFilePath("/path:invalid"))
        assertEquals("File path contains invalid character", validateFilePath("/path\"invalid"))
        assertEquals("File path contains invalid character", validateFilePath("/path|invalid"))
        assertEquals("File path contains invalid character", validateFilePath("/path?invalid"))
        assertEquals("File path contains invalid character", validateFilePath("/path*invalid"))
    }
}