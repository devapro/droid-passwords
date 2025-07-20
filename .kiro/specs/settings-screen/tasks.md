# Implementation Plan

- [x] 
    1. Create core data models and enums

    - Create LockInterval enum with time values and display names
    - Create ThemeMode enum for theme options
    - Create SettingsState data class for managing settings state
    - _Requirements: 3.1, 4.1_

- [x] 
    2. Implement SettingsRepository interface and implementation

    - Create SettingsRepository interface with methods for all settings operations
    - Implement SettingsRepositoryImpl using LocalStorage for persistence
    - Add methods for lock interval, theme mode, and vault file path management
    - _Requirements: 2.1, 3.1, 4.1_

- [x] 
    3. Create ThemeManager for theme handling

    - Implement ThemeManager class with LocalStorage integration
    - Add methods to get/set theme mode with Flow-based observation
    - Implement system theme detection logic
    - _Requirements: 4.1, 4.2, 4.3, 4.4, 4.5_

- [x] 
    4. Enhance LockManager with timer functionality

    - Add lock interval configuration to existing LockManager
    - Implement timer functionality for auto-lock feature
    - Add methods to start, stop, and reset lock timer
    - _Requirements: 3.2, 3.3, 3.4, 3.5, 3.6_

- [x] 
    5. Create MVI model classes for settings screen

    - Create SettingsScreenState sealed interface with Loading, Error, and Success states
    - Create SettingsScreenAction sealed interface with all user actions
    - Create SettingsScreenEvent sealed interface for navigation and side effects
    - _Requirements: 1.1, 2.1, 3.1, 4.1, 5.1_

- [x] 
    6. Implement settings screen reducers

    - Create InitScreenReducer to load initial settings state
    - Create OnLockIntervalChangedReducer for lock interval updates
    - Create OnThemeModeChangedReducer for theme mode updates
    - Create OnChangePasswordClickedReducer for password change dialog
    - Create OnFilePathClickedReducer for file path selection dialog
    - _Requirements: 1.1, 2.1, 3.1, 4.1_

- [x] 
    7. Create password change functionality

    - Create OnPasswordChangeSubmittedReducer with validation logic
    - Implement password validation (current password check, new password confirmation)
    - Integrate with VaultFileRepository.changePassword method
    - Add error handling for password change failures
    - _Requirements: 1.2, 1.3, 1.4, 1.5, 1.6_

- [x] 
    8. Implement file path selection functionality

    - Create OnFilePathSelectedReducer for handling path changes
    - Add file path validation and writability checks
    - Implement safe file migration logic for vault data
    - Add error handling for invalid paths and migration failures
    - _Requirements: 2.2, 2.3, 2.4, 2.5, 2.6_

- [x] 
    9. Create SettingsScreenActionProcessor

    - Implement ActionProcessor with all reducers
    - Add proper error handling and state management
    - Integrate with CoroutineContextProvider for async operations
    - _Requirements: 1.1, 2.1, 3.1, 4.1, 5.1_

- [x] 
    10. Create SettingsScreenViewModel

    - Implement MviViewModel subclass for settings screen
    - Wire up ActionProcessor and provide state/event flows
    - _Requirements: 5.1, 5.2, 5.3, 5.4_

- [x] 
    11. Create settings screen UI components

    - Create SettingItem composable for basic setting display
    - Create SettingClickableItem composable for actionable settings
    - Create SettingRadioGroup composable for multiple choice settings
    - Create reusable setting section headers and dividers
    - _Requirements: 1.1, 2.1, 3.1, 4.1_

- [x] 
    12. Implement change password dialog

    - Create ChangePasswordDialog composable with form fields
    - Add password validation UI feedback
    - Implement proper form state management
    - Add loading states and error display
    - _Requirements: 1.2, 1.3, 1.4, 1.5, 1.6_

- [x] 
    13. Implement file path selection dialog

    - Create FilePathSelectionDialog using FileKit
    - Add current path display and browse functionality
    - Implement path validation feedback
    - Add confirmation for path changes
    - _Requirements: 2.2, 2.3, 2.4, 2.5, 2.6_

- [x] 
    14. Create main SettingsScreenContent composable

    - Implement main settings screen layout with sections
    - Add "Change Password" clickable item
    - Add "Vault File Path" setting with current path display
    - Add lock interval radio group (15min/30min/never)
    - Add theme mode radio group (Dark/Light/System)
    - _Requirements: 1.1, 2.1, 3.1, 4.1_

- [x] 
    15. Create SettingsScreen navigation component

    - Create SettingsScreen class implementing Screen interface
    - Add navigation parameter handling if needed
    - _Requirements: 5.1, 5.2_

- [x] 
    16. Implement SettingsScreenRoot composable

    - Create root composable with ViewModel integration
    - Handle navigation events (back navigation)
    - Manage dialog state and display
    - Integrate with SnackbarHostStateManager for user feedback
    - _Requirements: 5.1, 5.2, 5.3, 5.4, 5.5_

- [x] 
    17. Create settings screen dependency injection

    - Create registerSettingsScreenDi function
    - Register all settings screen components with Koin
    - Add SettingsRepository and ThemeManager to DI container
    - _Requirements: 5.1_

- [x] 
    18. Integrate settings screen with main app DI

    - Add registerSettingsScreenDi call to main app module
    - Ensure all dependencies are properly wired
    - _Requirements: 5.1_

- [x] 
    19. Update password list screen navigation

    - Uncomment and implement settings navigation in PasswordListScreenRoot
    - Ensure settings menu item properly navigates to settings screen
    - _Requirements: 5.1, 5.2_

- [x] 
    20. Integrate theme changes with main App composable

    - Update App.kt to observe theme changes from ThemeManager
    - Apply theme changes to MaterialTheme wrapper
    - Ensure theme persistence across app restarts
    - _Requirements: 4.2, 4.3, 4.4, 4.5, 4.6_

- [ ] 
    21. Test settings screen functionality

    - Create unit tests for SettingsRepository implementation
    - Create unit tests for ThemeManager functionality
    - Create unit tests for enhanced LockManager timer features
    - Test MVI state management and action processing
    - _Requirements: 1.1, 2.1, 3.1, 4.1, 5.1_

- [ ] 
    22. Test integration and end-to-end functionality

    - Test navigation to and from settings screen
    - Test password change flow with vault re-encryption
    - Test file path changes with data migration
    - Test lock interval functionality with actual timing
    - Test theme changes applied immediately and persisted
    - _Requirements: 1.6, 2.6, 3.6, 4.6, 5.5_