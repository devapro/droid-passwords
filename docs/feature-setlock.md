# Feature: setlock

← [Back to index](./index.md) · [Architecture](./architecture.md) · modules: `features/setlock`, `features-api/setlock`

Manages the vault lock password. Used in three situations:

1. **First-launch setup** — chosen by the user from [welcome](./feature-welcome.md). The vault file is created with this password.
2. **Change password** — invoked from [settings](./feature-settings.md). Both the lock password in memory and the encrypted file are re-keyed.
3. **Remove password** — clear the in-memory lock password (the vault file remains encrypted with whatever it was created with).

## Navigation contract — `features-api/setlock`

```kotlin
data object SetLockPasswordScreen : Screen
interface SetLockPasswordScreenFactory { fun create(): SetLockPasswordScreen }
```

## MVI shape — `features/setlock`

**State** — `SetLockPasswordScreenState`
- `Loading`
- `Error`
- `Success(currentPassword, newPassword, confirmPassword, isCurrentVisible, isNewVisible, isConfirmVisible, isProcessing, currentPasswordError?, newPasswordError?, confirmPasswordError?, …)`

**Actions** — `SetLockPasswordScreenAction`
- `InitScreen`
- `OnCurrentPasswordChanged(value)`
- `OnNewPasswordChanged(value)`
- `OnConfirmPasswordChanged(value)`
- `OnToggleCurrentPasswordVisibility` / `OnToggleNewPasswordVisibility` / `OnToggleConfirmPasswordVisibility`
- `OnSaveClicked`
- `OnRemovePasswordClicked`
- `OnBackClicked`

**Events** — `SetLockPasswordScreenEvent`
- `NavigateBack`
- `RemovePassword`
- `ShowError(message)`
- `ShowSuccess(message)`

**Reducers** — one per action: `InitScreenReducer`, `OnBackClickedReducer`, `OnConfirmPasswordChangedReducer`, `OnCurrentPasswordChangedReducer`, `OnNewPasswordChangedReducer`, `OnRemovePasswordClickedReducer`, `OnSaveClickedReducer`, `OnToggleConfirmPasswordVisibilityReducer`, `OnToggleCurrentPasswordVisibilityReducer`, `OnToggleNewPasswordVisibilityReducer`.

`OnSaveClickedReducer` is the interesting one — it:
1. Validates that `newPassword == confirmPassword` and non-empty.
2. Branches on whether a lock password already exists:
   - **No existing password** → `LockManager.setLockPassword(newPassword)` and create the vault via `VaultFileRepository.createVault`.
   - **Existing password** → `LockManager.changeLockPassword(current, new)` and `VaultFileRepository.changePassword(current, new)`.
3. Emits `ShowSuccess` and `NavigateBack` on success, `ShowError` on failure.

## Dependencies

- `LockManager` — in-memory lock state.
- `VaultFileRepository` — actually writes / re-keys the encrypted vault file.

## UI

`SetLockPasswordScreenRoot` with three password fields (current / new / confirm), visibility toggles, **Save** and **Remove password** buttons. Inline validation messages come from the state. The "current password" field is hidden during initial setup.

## DI

`registerSetLockPasswordScreenDi()` — ViewModel, ActionProcessor, `InitStateFactory`, factory, and all 10 reducers.
