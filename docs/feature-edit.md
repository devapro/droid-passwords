# Feature: edit (add / edit password)

← [Back to index](./index.md) · [Architecture](./architecture.md) · modules: `features/edit`, `features-api/edit`

Same screen for both "create new password" and "edit existing one". The constructor argument decides which.

## Navigation contract — `features-api/edit`

```kotlin
data class AddEditPasswordScreen(
    val item: ItemModel?,                // null ⇒ create; non-null ⇒ edit
    val selectedTag: VaultItemTag?       // pre-selected tag when invoked from a tag context
) : Screen

interface AddEditPasswordScreenFactory {
    fun create(item: ItemModel?, selectedTag: VaultItemTag?): AddEditPasswordScreen
}
```

## MVI shape — `features/edit`

**State** — `AddEditPasswordScreenState`
- `Loading`, `Error`, `Success(title, username, password, url, description, additionalFields, tags, tagInput, isPasswordVisible, showAdditionalFields, isEditing, validationErrors, …)`

**Actions** — `AddEditPasswordScreenAction`

Form input:
- `OnTitleChanged`, `OnUsernameChanged`, `OnPasswordChanged`, `OnUrlChanged`, `OnDescriptionChanged`

Form behaviour:
- `OnTogglePasswordVisibility`
- `OnToggleAdditionalFields`
- `OnGeneratePassword`

Additional fields:
- `OnAdditionalFieldNameChanged(index, name)`
- `OnAdditionalFieldValueChanged(index, value)`
- `OnAddAdditionalField`
- `OnRemoveAdditionalField(index)`

Tags:
- `OnTagInputChanged(value)`
- `OnTagSelected(tag)`
- `OnTagRemoved(tag)`

Save / delete:
- `OnSaveClicked` → chains to `OnSave`
- `OnSave`
- `OnDeleteClicked`, `OnDeleteConfirmed`, `OnDeleteCancelled`

Navigation:
- `OnBackClicked`
- `InitScreen`

**Events** — `AddEditPasswordScreenEvent`
- `NavigateBack`, `SaveSuccess`, `DeleteSuccess`, `ShowMessage`, `GeneratedPassword(password)`

**Reducers (22)**: `InitScreenReducer`, `OnTitleChangedReducer`, `OnUsernameChangedReducer`, `OnPasswordChangedReducer`, `OnUrlChangedReducer`, `OnDescriptionChangedReducer`, `OnTogglePasswordVisibilityReducer`, `OnToggleAdditionalFieldsReducer`, `OnGeneratePasswordReducer`, `OnAdditionalFieldNameChangedReducer`, `OnAdditionalFieldValueChangedReducer`, `OnAddAdditionalFieldReducer`, `OnRemoveAdditionalFieldReducer`, `OnTagInputChangedReducer`, `OnTagSelectedReducer`, `OnTagRemovedReducer`, `OnSaveClickedReducer`, `OnSaveReducer`, `OnBackClickedReducer`, `OnDeleteClickedReducer`, `OnDeleteConfirmedReducer`, `OnDeleteCancelledReducer`.

`OnSaveClickedReducer` validates and chains to `OnSaveReducer`, which:
1. Builds the `VaultItemModel` from current state.
2. Calls `VaultRuntimeRepository.addOrUpdateVault(item)`.
3. Persists via `VaultFileRepository.saveVault(...)` (re-encrypts the whole file).
4. Updates `PasswordRepository` so the list screen sees the change.
5. Emits `SaveSuccess` + `NavigateBack`.

`OnGeneratePasswordReducer` produces a random password (length/character-class is configured in code) and writes it into state; the event echoes the generated string for any UI feedback that needs it.

## Dependencies

- `PasswordRepository` — mirror the UI list.
- `VaultRuntimeRepository` — source of truth (runtime).

`VaultFileRepository.saveVault` is invoked transitively to persist the change.

## UI

`AddEditPasswordScreenRoot` with:
- Title / username / password / URL / description fields.
- Password visibility toggle and generator.
- Collapsible "additional fields" section.
- Tag input (chip-style add/remove).
- **Save** in the top bar; **Delete** only when editing.

## DI

`registerAddEditPasswordScreenDi()` — ViewModel, ActionProcessor, `InitStateFactory`, `AddEditPasswordScreenFactory`, all 22 reducers.
