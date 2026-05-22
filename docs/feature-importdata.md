# Feature: importdata

← [Back to index](./index.md) · [Architecture](./architecture.md) · modules: `features/importdata`, `features-api/importdata`

Mirror of [export](./feature-export.md): imports passwords from a CSV, JSON, or encrypted DATA file into the current runtime vault.

| Format | Notes |
| --- | --- |
| `CSV` | Plaintext. Mapped by column name. |
| `JSON` | Plaintext serialised `VaultModel`. |
| `DATA` | Encrypted. The user must supply the password the source vault was created with — that's why the import screen has a password field. |

## Navigation contract — `features-api/importdata`

```kotlin
data object ImportScreen : Screen
interface ImportScreenFactory { fun create(): ImportScreen }
```

`FileFormat` is shared with export (`data/FileFormat.kt`).

## MVI shape — `features/importdata`

**State** — `ImportScreenState`
- `Loading`, `Error`, `Loaded(password, isPasswordVisible, selectedFormat, isProcessing, formats, …)`

**Actions** — `ImportScreenAction`
- `InitScreen`
- `OnFormatSelected(format)`
- `OnPasswordChanged(value)` / `OnTogglePasswordVisibility`
- `OnImportClicked` — opens the file picker.
- `ImportFileSelected(file)` / `ImportFileCancelled`
- `OnUnlockClicked` — only relevant for the DATA format.
- (and `OnBackClicked`)

**Events** — `ImportScreenEvent`
- `NavigateBack`, `OpenFileFor`, `OpenFileForImport`, `ShowError(msg)`, `ShowSuccess(msg)`

**Reducers** — `InitScreenReducer`, `OnBackClickedReducer`, `OnFormatSelectedReducer`, `OnImportClickedReducer`, `OnImportFileCancelledReducer`, `OnImportFileSelectedReducer`, `OnPasswordChangedReducer`, `OnTogglePasswordVisibilityReducer`.

When the user picks a source file, `OnImportFileSelectedReducer` dispatches by selected format:

```
ImportScreenAction.ImportFileSelected(file)
  └─► branch on state.selectedFormat
        CSV  ─► ImportFromCSVUseCase(file)
        JSON ─► ImportFromJsonUseCase(file)
        DATA ─► ImportFromDataUseCase(file, state.password)
                  └─► VaultFileRepository.getVaultFromSpecificFile(password, file)
```

The imported items are merged into `VaultRuntimeRepository`. The reducer also calls `VaultFileRepository.saveVault(...)` so the change is persisted to the on-disk vault.

## Dependencies

- `VaultFileRepository.getVaultFromSpecificFile` — decrypt an external DATA file.
- `VaultRuntimeRepository` — merge incoming items.
- `ImportFromCSVUseCase`, `ImportFromJsonUseCase`, `ImportFromDataUseCase` (local).
- File picker via **FileKit**, triggered by the `OpenFileForImport` event.

## UI

`ImportScreenRoot`:
- Format selector.
- Password field (only enabled for DATA).
- **Import** button.
- Snackbar messages.

## DI

`registerImportScreenDi()` — ViewModel, ActionProcessor, `InitStateFactory`, `ImportScreenFactory`, eight reducers, and the three import use cases.
