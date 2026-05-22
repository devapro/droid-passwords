# Feature: settings

← [Back to index](./index.md) · [Architecture](./architecture.md) · modules: `features/settings`, `features-api/settings`

App-wide preferences:

- **Lock interval** — how long after the last interaction the vault auto-locks (`LockInterval`).
- **Theme** — `LIGHT` / `DARK` / `SYSTEM`.
- **Vault file path** — where the encrypted vault is stored on disk.
- **Change password** — opens a dialog to re-key the vault (delegates the actual change to `LockManager` + `VaultFileRepository`).

## Navigation contract — `features-api/settings`

```kotlin
data object SettingsScreen : Screen
interface SettingsScreenFactory { fun create(): SettingsScreen }
```

## MVI shape — `features/settings`

**State** — `SettingsScreenState`
- `Loading`, `Error`, `Success(lockInterval, themeMode, vaultFilePath?, showChangePasswordDialog, showFilePathDialog, isProcessing, error?, success?)`

**Actions** — `SettingsScreenAction`
- `InitScreen`
- `OnLockIntervalChanged(interval)`
- `OnThemeModeChanged(mode)`
- `OnFilePathClicked` / `OnFilePathSelected(path)` / `OnDismissFilePathDialog`
- `OnChangePasswordClicked` / `OnPasswordChangeSubmitted(old, new)` / `OnDismissChangePasswordDialog`
- `OnBackClicked`

**Events** — `SettingsScreenEvent`
- `NavigateBack`, `ShowError(msg)`, `ShowSuccess(msg)`, `ShowFilePathPicker`

**Reducers** — `InitScreenReducer`, `OnBackClickedReducer`, `OnChangePasswordClickedReducer`, `OnDismissChangePasswordDialogReducer`, `OnDismissFilePathDialogReducer`, `OnFilePathClickedReducer`, `OnFilePathSelectedReducer`, `OnLockIntervalChangedReducer`, `OnPasswordChangeSubmittedReducer`, `OnThemeModeChangedReducer`.

- `OnLockIntervalChangedReducer` persists via `SettingsRepository` and calls `LockManager.setLockInterval(...)` so the timer applies immediately.
- `OnThemeModeChangedReducer` writes through `ThemeManager`; `App.kt` recomposes because it observes the manager's `Flow`.
- `OnPasswordChangeSubmittedReducer` calls `LockManager.changeLockPassword(old, new)` and `VaultFileRepository.changePassword(old, new)`. Emits `ShowSuccess` or `ShowError`.

## Dependencies

- `SettingsRepository` — DataStore-backed persistence.
- `ThemeManager` — exposes theme as a `Flow` for the rest of the app.
- `LockManager` — applies the new lock interval to the running timer; performs password change.
- `VaultFileRepository` — re-encrypts the file when the password changes.

## UI

`SettingsScreenRoot` — settings list (a section per preference), modal dialogs for change-password and file-path selection.

## DI

`registerSettingsScreenDi()` — ViewModel, ActionProcessor, `InitStateFactory`, `SettingsScreenFactory`, `SettingsRepository`, `ThemeManager` (already singleton in core/data DI), and the ten reducers.
