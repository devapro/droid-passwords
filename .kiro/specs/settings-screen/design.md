# Settings Screen Design Document

## Overview

The Settings Screen will be implemented following the existing MVI (Model-View-Intent) architecture
pattern used throughout the application. It will provide users with configuration options for
password management, file storage, security settings, and theme preferences. The screen will
integrate seamlessly with the current navigation system using Voyager and maintain consistency with
the existing UI design patterns.

## Architecture

### MVI Pattern Implementation

The settings screen will follow the established MVI pattern with these core components:

- **SettingsScreenState**: Represents the current state of the settings screen
- **SettingsScreenAction**: Defines user actions and system events
- **SettingsScreenEvent**: Represents navigation and side effects
- **SettingsScreenViewModel**: Manages state and coordinates actions
- **SettingsScreenActionProcessor**: Processes actions and updates state
- **SettingsScreenRoot**: Composable root that handles navigation and events

### Navigation Integration

The settings screen will integrate with the existing Voyager navigation system:

- Accessible from the password list screen via the overflow menu
- Implements standard back navigation
- Supports deep navigation for specific settings (e.g., change password dialog)

## Components and Interfaces

### Data Layer Components

#### SettingsRepository

```kotlin
interface SettingsRepository {
    suspend fun getLockInterval(): LockInterval
    suspend fun setLockInterval(interval: LockInterval)
    suspend fun getThemeMode(): ThemeMode
    suspend fun setThemeMode(mode: ThemeMode)
    suspend fun getVaultFilePath(): String
    suspend fun setVaultFilePath(path: String): Boolean
    suspend fun changeVaultPassword(oldPassword: String, newPassword: String): AppResult<Unit>
}
```

#### Enhanced LockManager

The existing LockManager will be extended to support configurable lock intervals:

```kotlin
object LockManager {
    // Existing functionality...
    fun setLockInterval(interval: LockInterval)
    fun startLockTimer()
    fun resetLockTimer()
    fun stopLockTimer()
}
```

#### ThemeManager

New component to manage theme preferences:

```kotlin
class ThemeManager(private val localStorage: LocalStorage) {
    suspend fun setThemeMode(mode: ThemeMode)
    fun getThemeMode(): Flow<ThemeMode>
    fun getCurrentTheme(): ThemeMode
}
```

### UI Layer Components

#### SettingsScreenContent

Main composable that renders the settings UI with sections for:

- Password Management
- File Storage
- Security Settings
- Theme Preferences

#### Setting Item Components

Reusable components for different setting types:

- `SettingItem`: Basic setting with title and subtitle
- `SettingClickableItem`: Clickable setting that triggers actions
- `SettingRadioGroup`: Radio button group for multiple choice settings
- `SettingSwitch`: Toggle switch for boolean settings

## Data Models

### Core Data Models

```kotlin
enum class LockInterval(val minutes: Int, val displayName: String) {
    FIFTEEN_MINUTES(15, "15 minutes"),
    THIRTY_MINUTES(30, "30 minutes"),
    NEVER(0, "Never")
}

enum class ThemeMode(val displayName: String) {
    LIGHT("Light"),
    DARK("Dark"),
    SYSTEM("System")
}

data class SettingsState(
    val currentLockInterval: LockInterval,
    val currentThemeMode: ThemeMode,
    val vaultFilePath: String,
    val isLoading: Boolean = false,
    val error: String? = null
)
```

### MVI Models

```kotlin
sealed interface SettingsScreenState {
    data object Loading : SettingsScreenState
    data class Error(val message: String) : SettingsScreenState
    data class Success(
        val lockInterval: LockInterval,
        val themeMode: ThemeMode,
        val vaultFilePath: String,
        val isChangePasswordDialogVisible: Boolean = false,
        val isFilePathDialogVisible: Boolean = false
    ) : SettingsScreenState
}

sealed interface SettingsScreenAction {
    data object InitScreen : SettingsScreenAction
    data object OnBackClicked : SettingsScreenAction
    data object OnChangePasswordClicked : SettingsScreenAction
    data object OnFilePathClicked : SettingsScreenAction
    data class OnLockIntervalChanged(val interval: LockInterval) : SettingsScreenAction
    data class OnThemeModeChanged(val mode: ThemeMode) : SettingsScreenAction
    data class OnPasswordChangeSubmitted(
        val currentPassword: String,
        val newPassword: String,
        val confirmPassword: String
    ) : SettingsScreenAction
    data class OnFilePathSelected(val path: String) : SettingsScreenAction
    data object OnDismissChangePasswordDialog : SettingsScreenAction
    data object OnDismissFilePathDialog : SettingsScreenAction
}

sealed interface SettingsScreenEvent {
    data object NavigateBack : SettingsScreenEvent
    data class ShowError(val message: String) : SettingsScreenEvent
    data class ShowSuccess(val message: String) : SettingsScreenEvent
    data object ShowFilePathPicker : SettingsScreenEvent
}
```

## Error Handling

### Password Change Errors

- Invalid current password
- New password validation failures
- Vault re-encryption failures
- Network/storage errors

### File Path Errors

- Invalid path selection
- Permission denied errors
- Storage space issues
- File migration failures

### Theme/Lock Interval Errors

- Preference storage failures
- Invalid configuration values

### Error Display Strategy

- Use SnackbarHostStateManager for temporary error messages
- Display inline errors for form validation
- Provide retry mechanisms for recoverable errors

## Testing Strategy

### Unit Tests

- SettingsRepository implementation tests
- ThemeManager functionality tests
- LockManager timer functionality tests
- MVI state management tests
- Validation logic tests

### Integration Tests

- Settings persistence across app restarts
- Theme changes applied correctly
- Lock timer functionality
- Password change flow
- File path migration

### UI Tests

- Settings screen navigation
- Dialog interactions
- Form validation feedback
- Theme switching visual verification
- Accessibility compliance

## Implementation Considerations

### Performance

- Lazy loading of settings values
- Efficient theme switching without full recomposition
- Background file operations for path changes
- Debounced user input handling

### Security

- Secure password validation
- Safe file path validation
- Encrypted storage of sensitive settings
- Proper cleanup of sensitive data in memory

### Platform Compatibility

- File path selection adapted per platform
- Theme system integration per platform
- Lock timer implementation per platform
- Storage location defaults per platform

### Accessibility

- Proper content descriptions for all interactive elements
- Screen reader support for setting values
- Keyboard navigation support
- High contrast theme support

## Dependencies

### New Dependencies

- FileKit for file path selection (already available)
- Platform-specific theme detection utilities

### Enhanced Existing Dependencies

- LocalStorage for settings persistence
- LockManager for timer functionality
- VaultFileRepository for password changes
- SnackbarHostStateManager for user feedback

## Migration Strategy

### Settings Migration

- Default values for new settings
- Backward compatibility with existing preferences
- Graceful handling of missing settings
- Version-based migration logic

### File Path Migration

- Safe migration of existing vault files
- Rollback capability for failed migrations
- User confirmation for destructive operations
- Backup creation before migration