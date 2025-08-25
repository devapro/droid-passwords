package io.github.devapro.data

import io.github.devapro.data.model.LockInterval
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LockManagerTest {

    @Test
    fun testInitialState() {
        // Test initial state of LockManager
        assertTrue(LockManager.isLocked)
        assertFalse(LockManager.hasLockPassword)
        assertTrue(LockManager.isFirstLaunch)
        assertEquals(LockInterval.NEVER, LockManager.lockInterval)
    }

    @Test
    fun testSetLockPassword() {
        // Reset state
        LockManager.removeLockPassword()

        LockManager.setLockPassword("testPassword")

        assertTrue(LockManager.hasLockPassword)
        assertTrue(LockManager.isLocked)
        assertFalse(LockManager.isFirstLaunch)
    }

    @Test
    fun testValidatePassword() {
        LockManager.setLockPassword("correctPassword")

        assertTrue(LockManager.validatePassword("correctPassword"))
        assertFalse(LockManager.validatePassword("wrongPassword"))
        assertFalse(LockManager.validatePassword(""))
    }

    @Test
    fun testUnlock() {
        LockManager.setLockPassword("testPassword")

        // Test successful unlock
        assertTrue(LockManager.unlock("testPassword"))
        assertFalse(LockManager.isLocked)

        // Test failed unlock
        LockManager.lock()
        assertFalse(LockManager.unlock("wrongPassword"))
        assertTrue(LockManager.isLocked)
    }

    @Test
    fun testLock() {
        LockManager.setLockPassword("testPassword")
        LockManager.unlock("testPassword")
        assertFalse(LockManager.isLocked)

        LockManager.lock()
        assertTrue(LockManager.isLocked)
    }

    @Test
    fun testRemoveLockPassword() {
        LockManager.setLockPassword("testPassword")
        assertTrue(LockManager.hasLockPassword)

        LockManager.removeLockPassword()
        assertFalse(LockManager.hasLockPassword)
        assertFalse(LockManager.isLocked)
    }

    @Test
    fun testCompleteFirstLaunch() {
        // Reset to initial state
        LockManager.removeLockPassword()

        assertTrue(LockManager.isFirstLaunch)

        LockManager.completeFirstLaunch()
        assertFalse(LockManager.isFirstLaunch)
    }

    @Test
    fun testChangeLockPassword() {
        LockManager.setLockPassword("oldPassword")

        // Test successful password change
        assertTrue(LockManager.changeLockPassword("oldPassword", "newPassword"))
        assertTrue(LockManager.validatePassword("newPassword"))
        assertFalse(LockManager.validatePassword("oldPassword"))

        // Test failed password change with wrong old password
        assertFalse(LockManager.changeLockPassword("wrongOldPassword", "anotherNewPassword"))
        assertTrue(LockManager.validatePassword("newPassword")) // Should still be the previous password
    }

    @Test
    fun testSetLockInterval() {
        assertEquals(LockInterval.NEVER, LockManager.lockInterval)

        LockManager.setLockInterval(LockInterval.FIFTEEN_MINUTES)
        assertEquals(LockInterval.FIFTEEN_MINUTES, LockManager.lockInterval)

        LockManager.setLockInterval(LockInterval.THIRTY_MINUTES)
        assertEquals(LockInterval.THIRTY_MINUTES, LockManager.lockInterval)

        LockManager.setLockInterval(LockInterval.NEVER)
        assertEquals(LockInterval.NEVER, LockManager.lockInterval)
    }

    @Test
    fun testStartLockTimer_WithNeverInterval() {
        LockManager.setLockPassword("testPassword")
        LockManager.unlock("testPassword")
        LockManager.setLockInterval(LockInterval.NEVER)

        // Timer should not start with NEVER interval
        LockManager.startLockTimer()
        assertFalse(LockManager.isLocked) // Should remain unlocked
    }

    @Test
    fun testStartLockTimer_WhenLocked() {
        LockManager.setLockPassword("testPassword")
        LockManager.setLockInterval(LockInterval.FIFTEEN_MINUTES)
        assertTrue(LockManager.isLocked)

        // Timer should not start when already locked
        LockManager.startLockTimer()
        assertTrue(LockManager.isLocked) // Should remain locked
    }

    @Test
    fun testStartLockTimer_WithoutPassword() {
        LockManager.removeLockPassword()
        LockManager.setLockInterval(LockInterval.FIFTEEN_MINUTES)

        // Timer should not start without a password set
        LockManager.startLockTimer()
        assertFalse(LockManager.isLocked) // Should remain unlocked
    }

    @Test
    fun testStopLockTimer() {
        LockManager.setLockPassword("testPassword")
        LockManager.unlock("testPassword")
        LockManager.setLockInterval(LockInterval.FIFTEEN_MINUTES)

        LockManager.startLockTimer()
        LockManager.stopLockTimer()

        // Timer should be stopped, so app should remain unlocked
        assertFalse(LockManager.isLocked)
    }

    @Test
    fun testResetLockTimer() {
        LockManager.setLockPassword("testPassword")
        LockManager.unlock("testPassword")
        LockManager.setLockInterval(LockInterval.FIFTEEN_MINUTES)

        // Reset timer should work when conditions are met
        LockManager.resetLockTimer()
        assertFalse(LockManager.isLocked) // Should remain unlocked after reset
    }

    @Test
    fun testResetLockTimer_WithNeverInterval() {
        LockManager.setLockPassword("testPassword")
        LockManager.unlock("testPassword")
        LockManager.setLockInterval(LockInterval.NEVER)

        // Reset timer should not start timer with NEVER interval
        LockManager.resetLockTimer()
        assertFalse(LockManager.isLocked) // Should remain unlocked
    }

    @Test
    fun testResetLockTimer_WhenLocked() {
        LockManager.setLockPassword("testPassword")
        LockManager.setLockInterval(LockInterval.FIFTEEN_MINUTES)
        assertTrue(LockManager.isLocked)

        // Reset timer should not work when locked
        LockManager.resetLockTimer()
        assertTrue(LockManager.isLocked) // Should remain locked
    }

    @Test
    fun testResetLockTimer_WithoutPassword() {
        LockManager.removeLockPassword()
        LockManager.setLockInterval(LockInterval.FIFTEEN_MINUTES)

        // Reset timer should not work without password
        LockManager.resetLockTimer()
        assertFalse(LockManager.isLocked) // Should remain unlocked
    }

    @Test
    fun testLockIntervalValues() {
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
    fun testLockIntervalToString() {
        // Test that lock intervals convert to proper string values
        assertEquals("FIFTEEN_MINUTES", LockInterval.FIFTEEN_MINUTES.name)
        assertEquals("THIRTY_MINUTES", LockInterval.THIRTY_MINUTES.name)
        assertEquals("NEVER", LockInterval.NEVER.name)
    }

    // Note: Testing actual timer functionality with delays would be complex and flaky
    // in unit tests. The timer logic is tested through the public API methods above.
    // Integration tests would be more appropriate for testing the actual timing behavior.
}