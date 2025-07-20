# Requirements Document

## Introduction

This document outlines the requirements for implementing a Settings Screen in the password manager
application. The settings screen will allow users to configure application preferences including
password management, file storage location, security settings, and theme preferences. The screen
should follow the existing MVI architecture pattern and integrate seamlessly with the current
navigation structure.

## Requirements

### Requirement 1

**User Story:** As a user, I want to change my master password, so that I can maintain security and
update my credentials when needed.

#### Acceptance Criteria

1. WHEN the user navigates to settings THEN the system SHALL display a "Change Password" option
2. WHEN the user clicks "Change Password" THEN the system SHALL prompt for current password, new
   password, and confirmation
3. WHEN the user enters valid current password and matching new passwords THEN the system SHALL
   update the master password and re-encrypt the vault
4. WHEN the user enters incorrect current password THEN the system SHALL display an error message
5. WHEN new password and confirmation don't match THEN the system SHALL display a validation error
6. WHEN password change is successful THEN the system SHALL display a success message and return to
   settings

### Requirement 2

**User Story:** As a user, I want to configure the temporary password file path, so that I can
control where my encrypted data is stored.

#### Acceptance Criteria

1. WHEN the user navigates to settings THEN the system SHALL display the current tmp file path
2. WHEN the user clicks on the file path setting THEN the system SHALL allow browsing and selecting
   a new directory
3. WHEN a new path is selected THEN the system SHALL validate the path is writable
4. WHEN the path is valid THEN the system SHALL update the storage location and move existing data
   if present
5. WHEN the path is invalid or not writable THEN the system SHALL display an error message
6. WHEN no path is selected THEN the system SHALL use the default cache directory

### Requirement 3

**User Story:** As a user, I want to set an auto-lock interval, so that my passwords are
automatically secured after a period of inactivity.

#### Acceptance Criteria

1. WHEN the user navigates to settings THEN the system SHALL display lock interval options (
   15min/30min/never)
2. WHEN the user selects a lock interval THEN the system SHALL save the preference
3. WHEN 15 minutes is selected THEN the system SHALL automatically lock after 15 minutes of
   inactivity
4. WHEN 30 minutes is selected THEN the system SHALL automatically lock after 30 minutes of
   inactivity
5. WHEN "never" is selected THEN the system SHALL not automatically lock the application
6. WHEN the app is locked due to timeout THEN the system SHALL require password to unlock

### Requirement 4

**User Story:** As a user, I want to choose between dark, light, or system theme modes, so that I
can customize the app appearance to my preference.

#### Acceptance Criteria

1. WHEN the user navigates to settings THEN the system SHALL display theme options (
   Dark/Light/System)
2. WHEN the user selects "Dark" THEN the system SHALL apply dark theme immediately
3. WHEN the user selects "Light" THEN the system SHALL apply light theme immediately
4. WHEN the user selects "System" THEN the system SHALL follow the device's system theme setting
5. WHEN system theme changes and "System" is selected THEN the system SHALL automatically update the
   app theme
6. WHEN a theme is selected THEN the system SHALL persist the preference across app restarts

### Requirement 5

**User Story:** As a user, I want to navigate to settings from the main password list, so that I can
easily access configuration options.

#### Acceptance Criteria

1. WHEN the user is on the password list screen THEN the system SHALL display a settings button/menu
   item
2. WHEN the user clicks the settings option THEN the system SHALL navigate to the settings screen
3. WHEN the user is on the settings screen THEN the system SHALL provide a way to navigate back
4. WHEN the user navigates back THEN the system SHALL return to the previous screen
5. WHEN settings are changed THEN the system SHALL apply changes immediately without requiring app
   restart