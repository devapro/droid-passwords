# Feature: unlock

← [Back to index](./index.md) · [Architecture](./architecture.md) · modules: `features/unlock`, `features-api/unlock`

Asks the user for their vault password, decrypts the vault file, and hands the result to `VaultRuntimeRepository` so the rest of the app can read it. Shown both at app start (when a vault exists) and after an auto-lock timeout.

## Flow

```
UnLockVaultScreen ── OnPasswordChanged ─► state.password updated
                  └─ OnUnlockClicked ──► validate format
                                           └─► UnlockVault
                                                  └─► VaultFileRepository.getVault(password)
                                                         success ─► VaultRuntimeRepository.loadVault(model)
                                                                    LockManager.unlock(password)
                                                                    event: UnlockSuccess ─► navigate to PasswordListScreen
                                                         failure ─► state.passwordError, event: ShowError
```

## Navigation contract — `features-api/unlock`

```kotlin
data object UnLockVaultScreen : Screen
interface UnLockVaultScreenFactory { fun create(): UnLockVaultScreen }
```

## MVI shape — `features/unlock`

**State** — `UnLockVaultScreenState`
- `Loading`
- `Loaded(password, isPasswordVisible, isProcessing, passwordError?, isValid)`

**Actions** — `UnLockVaultScreenAction`
- `InitScreen`
- `OnPasswordChanged(value)`
- `OnTogglePasswordVisibility`
- `OnUnlockClicked` — validates format, chains to `UnlockVault`.
- `UnlockVault` — calls the repositories.
- `OnBackClicked`

**Events** — `UnLockVaultScreenEvent`
- `NavigateBack`
- `ShowError(message)`
- `UnlockSuccess`

**Reducers**
- `InitScreenReducer`, `OnBackClickedReducer`, `OnPasswordChangedReducer`, `OnTogglePasswordVisibilityReducer`, `OnUnlockClickedReducer`, `UnlockVaultReducer`.

`OnUnlockClickedReducer` returns `Reducer.Result(state = loading, action = UnlockVault)` to chain the heavy I/O step. `UnlockVaultReducer` performs the actual decryption.

## Dependencies

- `VaultFileRepository.getVault(password)` — decrypts the file.
- `VaultRuntimeRepository.loadVault(model)` — installs the decrypted vault in memory.
- `LockManager.unlock(password)` — clears the locked flag and starts the auto-lock timer.

## UI

`UnLockVaultScreenRoot` — password field with visibility toggle, **Unlock** button, inline error.

## DI

`registerUnLockVaultScreenDi()` — ViewModel, ActionProcessor, `InitStateFactory`, factory, six reducers.
