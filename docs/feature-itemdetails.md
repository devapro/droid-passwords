# Feature: itemdetails (password detail)

← [Back to index](./index.md) · [Architecture](./architecture.md) · modules: `features/itemdetails`, `features-api/itemdetails`

Shows a single password with all its fields, with actions to copy a field, share, edit, or delete.

## Navigation contract — `features-api/itemdetails`

```kotlin
data class PasswordDetailScreen(val item: ItemModel) : Screen
interface PasswordDetailScreenFactory {
    fun create(item: ItemModel): PasswordDetailScreen
}
```

The `item` is the snapshot at navigation time. The reducer uses its `id` to look up the latest version (an item could have been edited via another path), but display is otherwise driven by what's passed in.

## MVI shape — `features/itemdetails`

**State** — `PasswordDetailScreenState`
- `Loading`, `Error`, `Success(item, isPasswordVisible, showDeleteConfirmation)`

**Actions** — `PasswordDetailScreenAction`
- `InitScreen`
- `OnEditClicked`
- `OnDeleteClicked`, `OnDeleteConfirmed`, `OnDeleteCancelled`
- `OnBackClicked`
- `OnTogglePasswordVisibility`
- `OnCopyField(field)`
- `OnShareClicked`

**Events** — `PasswordDetailScreenEvent`
- `NavigateBack`
- `NavigateToEdit(item)`
- `CopyToClipboard(text)`
- `ShareItem(item)`
- `DeleteItem(id)`
- `ShowMessage(text)`

**Reducers** — one per action: `InitScreenReducer`, `OnBackClickedReducer`, `OnCopyFieldReducer`, `OnDeleteCancelledReducer`, `OnDeleteClickedReducer`, `OnDeleteConfirmedReducer`, `OnEditClickedReducer`, `OnShareClickedReducer`, `OnTogglePasswordVisibilityReducer`.

`OnDeleteConfirmedReducer` calls `PasswordRepository.deletePassword(id)`, mirrors the change into `VaultRuntimeRepository`, and emits `DeleteItem` + `NavigateBack`.

`OnCopyFieldReducer` is purely event-emitting — the actual clipboard write happens in `ScreenRoot` so the side-effect stays out of reducers (platforms have different clipboard APIs).

## Dependencies

- `PasswordRepository` — delete.
- `VaultRuntimeRepository` — keep the runtime model in sync.

## UI

`PasswordDetailScreenRoot`:
- Header with the item title.
- Field rows (username, password [masked], URL, description, custom fields, tags).
- Per-row copy button.
- Top bar actions: edit, share, delete.
- Delete confirmation dialog driven by `state.showDeleteConfirmation`.

## DI

`registerPasswordDetailScreenDi()` — ViewModel, ActionProcessor, `InitStateFactory`, `PasswordDetailScreenFactory`, nine reducers.
