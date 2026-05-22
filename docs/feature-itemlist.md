# Feature: itemlist (password list)

← [Back to index](./index.md) · [Architecture](./architecture.md) · modules: `features/itemlist`, `features-api/itemlist`

The main screen after the vault is unlocked. Shows every password as a scrollable list with live-search and supports navigating to detail / edit / settings / export / tag filtering.

## Navigation contract — `features-api/itemlist`

```kotlin
data class PasswordListScreen(
    val type: PasswordTagFilterType,
    val tag: VaultItemTag?
) : Screen

enum class PasswordTagFilterType { ALL, NO_TAG, NORMAL }

interface PasswordListScreenFactory {
    fun create(type: PasswordTagFilterType, tag: VaultItemTag?): PasswordListScreen
}
```

| Filter type | Behaviour |
| --- | --- |
| `ALL` | Show every password. |
| `NO_TAG` | Show only items with no tags. |
| `NORMAL` | Show items that contain the given `tag`. |

## MVI shape — `features/itemlist`

**State** — `PasswordListScreenState`
- `Loading`, `Error`, `Success(title, selectedTag?, passwords, filteredPasswords, searchQuery, isLoading, isRefreshing, hasSearchQuery)`

**Actions** — `PasswordListScreenAction`
- `InitScreen`
- `OnSearchChanged(query)` / `OnClearSearch`
- `OnPasswordItemClicked(item)`
- `OnDeletePasswordClicked(id)`
- `OnAddPasswordClicked`
- `OnExportClicked`, `OnSettingsClicked`
- `OnBackClicked`

**Events** — `PasswordListScreenEvent`
- `NavigateToAddPassword`, `NavigateToPasswordDetail(item)`, `NavigateToExport`, `NavigateToSettings`, `DeletePassword(id)`, `OnBackClicked`

**Reducers** — `InitScreenReducer`, `OnAddPasswordClickedReducer`, `OnBackClickedReducer`, `OnClearSearchReducer`, `OnDeletePasswordClickedReducer`, `OnExportClickedReducer`, `OnPasswordItemClickedReducer`, `OnSearchChangedReducer`, `OnSettingsClickedReducer`.

`InitScreenReducer` reads from `VaultRuntimeRepository.getVault()`, applies the filter described by `type`/`tag`, maps `VaultItemModel` → `ItemModel` via `VaultItemMapper`, and pushes everything into `PasswordRepository`.

`OnSearchChangedReducer` filters in-memory and writes the result into `state.filteredPasswords` — it does not touch repositories.

## Dependencies

- `VaultRuntimeRepository` — source of truth for items.
- `PasswordRepository` — mirrored list the UI consumes.
- Local `VaultItemMapper` for `VaultItemModel` → `ItemModel`.

## UI

`PasswordListScreenRoot` with:
- Top app bar (title + settings/export actions).
- Search bar.
- Lazy list of password rows.
- Floating action button → add password.
- Delete confirmation dialog.

## DI

`registerPasswordListScreenDi()` — ViewModel, ActionProcessor, `InitStateFactory`, `PasswordListScreenFactory`, `VaultItemMapper`, and the nine reducers.
