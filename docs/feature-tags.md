# Feature: tags

← [Back to index](./index.md) · [Architecture](./architecture.md) · modules: `features/tags`, `features-api/tags`

Lists every tag used across the vault, with search. Tapping a tag opens a filtered [itemlist](./feature-itemlist.md) for that tag.

## Navigation contract — `features-api/tags`

```kotlin
data object TagsScreen : Screen
interface TagsScreenFactory { fun create(): TagsScreen }

data class TagItemModel(val tag: VaultItemTag, val type: TagItemType, val count: Int)
enum class TagItemType { TAG, NO_TAG, ALL }
```

`TagItemType` lets the screen render synthetic rows (e.g. "All" and "Untagged") next to the user-defined tags.

## MVI shape — `features/tags`

**State** — `TagsScreenState`
- `Loading`, `Success(tags, filteredTags, searchQuery, isLoading, isRefreshing, hasSearchQuery)`

**Actions** — `TagsScreenAction`
- `InitScreen` / `OnRefresh`
- `OnSearchChanged(query)` / `OnClearSearch`
- `OnTagClicked(tag)`
- `OnBackClicked`
- `OnAddPasswordClicked`, `OnExportClicked`, `OnSettingsClicked`

**Events** — `TagsScreenEvent`
- `NavigateToTagDetail(tag)` — opens `PasswordListScreen(type, tag)`
- `NavigateBack`, `NavigateToAddPassword`, `NavigateToExport`, `NavigateToSettings`

**Reducers** — `InitScreenReducer`, `OnAddPasswordClickedReducer`, `OnBackClickedReducer`, `OnClearSearchReducer`, `OnImportExportClickedReducer`, `OnRefreshReducer`, `OnSearchChangedReducer`, `OnSettingsClickedReducer`, `OnTagClickedReducer`.

`InitScreenReducer` / `OnRefreshReducer` read `VaultRuntimeRepository.getAllTags()` and count items per tag using a `TagsMapper`. Synthetic rows are added so the screen always shows "All" and "Untagged" at the top.

## Dependencies

- `VaultRuntimeRepository` — tag enumeration and counts.
- `TagsMapper` (local) — turns vault data into UI rows.

## UI

`TagsScreenRoot`:
- Search bar.
- Lazy list of tag rows (each shows tag name + item count).
- FAB to create a new password.

## DI

`registerTagsScreenDi()` — ViewModel, ActionProcessor, `InitStateFactory`, `TagsScreenFactory`, `TagsMapper`, nine reducers.
