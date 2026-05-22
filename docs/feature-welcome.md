# Feature: welcome

← [Back to index](./index.md) · [Architecture](./architecture.md) · modules: `features/welcome`, `features-api/welcome`

The very first screen shown on app launch. It checks whether a vault file already exists and routes the user accordingly.

## Routing

| Vault exists? | Next screen |
| --- | --- |
| No | `SetLockPasswordScreen` — user creates a vault with a chosen password |
| Yes | `UnLockVaultScreen` — user enters the existing password |

The user can also explicitly choose **Open existing vault** to pick a file from the filesystem (useful when moving between devices).

## Navigation contract — `features-api/welcome`

```kotlin
data object WelcomeScreen : Screen
interface WelcomeScreenFactory { fun create(): WelcomeScreen }
```

## MVI shape — `features/welcome`

**State** — `WelcomeScreenState`
- `Loading` — while checking the vault file.
- `Error`
- `Success(isVaultExists: Boolean)`

**Actions** — `WelcomeScreenAction`
- `InitScreen` — check `VaultFileRepository.isVaultExists`.
- `OnCreateNewVault`
- `OnLoadVault`
- `OnOpenExistingVault`

**Events** — `WelcomeScreenEvent`
- `OnCreateNewVault` — navigate to `SetLockPasswordScreen`.
- `OnOpenExistingVault` — open file picker / route to import.
- `OnLoadVault` — navigate to `UnLockVaultScreen`.

**Reducers**
- `InitScreenReducer` — asks `VaultFileRepository.isVaultExists()` and produces the appropriate `Success` state.
- `OnCreateNewVaultReducer` — emits `OnCreateNewVault` event.
- `OnLoadVaultReducer` — emits `OnLoadVault` event.
- `OnOpenExistingVaultReducer` — emits `OnOpenExistingVault` event.

## Dependencies

- `VaultFileRepository` (read-only; just calls `isVaultExists()`).

## UI

A single `WelcomeScreenRoot` Composable. When the vault is missing, it shows a "Create vault" CTA. When the vault exists, it shows an "Unlock" CTA. The "Open existing vault" option is available either way.

## DI

`registerWelcomeScreenDi()` — registers ViewModel, ActionProcessor, `InitStateFactory`, `WelcomeScreenFactory` implementation, and the four reducers.
