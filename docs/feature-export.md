# Feature: export

← [Back to index](./index.md) · [Architecture](./architecture.md) · modules: `features/export`, `features-api/export`

Exports the current vault to a user-chosen file. Three formats are supported:

| Format | Content | Encrypted? |
| --- | --- | --- |
| `CSV` | One row per item, columns for the standard fields. | No |
| `JSON` | The full `VaultModel` serialised to JSON. | No |
| `DATA` | The native vault format (same as `droid-d4.data`). | Yes — same encryption as the live vault. |

CSV and JSON exports are plaintext and warn the user accordingly.

## Navigation contract — `features-api/export`

```kotlin
data object ExportScreen : Screen
interface ExportScreenFactory { fun create(): ExportScreen }
```

`FileFormat` (CSV / JSON / DATA) lives in `data/FileFormat.kt`.

## MVI shape — `features/export`

**State** — `ExportScreenState`
- `Loading`, `Error`, `Loaded(selectedFormat, isProcessing, formats, description)`

**Actions** — `ExportScreenAction`
- `InitScreen`
- `OnFormatSelected(format)`
- `OnExportClicked` — opens the file picker.
- `ExportFileSelected(file)` / `ExportFileCancelled`
- `OnBackClicked`

**Events** — `ExportScreenEvent`
- `NavigateBack`, `OpenFileForExport(suggestedName, extension)`, `ShowError(msg)`, `ShowSuccess(msg)`

**Reducers** — `InitScreenReducer`, `OnFormatSelectedReducer`, `OnExportClickedReducer`, `OnBackClickedReducer`, `OnExportFileCancelledReducer`, `OnExportFileSelectedReducer`.

When the user picks a destination, `OnExportFileSelectedReducer` dispatches to the right use case:

```
ExportScreenAction.ExportFileSelected(file)
  └─► branch on state.selectedFormat
        CSV  ─► SaveCSVFileUseCase(file, vault)
        JSON ─► SaveJsonFileUseCase(file, vault)
        DATA ─► SaveDataFileUseCase(file, vault)
```

Each use case reads `VaultRuntimeRepository.getVault()` and writes via `FileKit.PlatformFile` (and `VaultFileRepository.saveVaultToSpecificFile` for DATA).

## Dependencies

- `VaultRuntimeRepository` — source data.
- `SaveCSVFileUseCase`, `SaveJsonFileUseCase`, `SaveDataFileUseCase` (local to the feature).
- File picker — provided by **FileKit**, triggered by the `OpenFileForExport` event handled in `ScreenRoot`.

## UI

`ExportScreenRoot`:
- Radio list of formats (with a one-line description of each).
- **Export** button — disabled while `isProcessing`.
- Snackbar messages on success/failure.

## DI

`registerExportScreenDi()` — ViewModel, ActionProcessor, `InitStateFactory`, `ExportScreenFactory`, six reducers, and the three use cases.
