---
paths:
  - "**/ui/**/*.kt"
  - "**/*ScreenRoot.kt"
  - "**/*ScreenContent.kt"
  - "**/*Screen.kt"
---

## Compose UI Rules

Composables in this project are Compose Multiplatform (`commonMain`) — keep them platform-agnostic
and free of business logic.

### Parameters — State and Callbacks Only

Composables accept State data and callbacks. Never pass a ViewModel, repository, mapper, or
`Navigator` as a parameter.

```kotlin
// ❌ WRONG
@Composable fun PasswordListScreenContent(viewModel: PasswordListScreenViewModel)

// ✅ CORRECT
@Composable fun PasswordListScreenContent(state: State.Success, onAction: (Action) -> Unit)
```

### No Business Logic, Mapping, or Formatting in Composables

Composables read State and dispatch Actions. No filtering, `.map()`, `.format()`,
string concatenation, or repository/datastore reads inside a Composable body — that work belongs
in the reducer, with ready-to-render values placed in State.

### `koinInject()` Only at Root / Screen Level

Resolve dependencies (the ViewModel) in the body of the screen root Composable. Never call
`koinInject()` in a child Composable and never pass an injected dependency down as a parameter
(not even as a default value).

```kotlin
// ✅ CORRECT
@Composable
fun PasswordListScreenRoot(...) {
    val viewModel: PasswordListScreenViewModel = koinInject()
    val state by viewModel.state.collectAsState()
    ...
}
```

### File Organization — Root vs Content Separated

- `<Feature>ScreenRoot.kt` — entry-point Composable wired to the ViewModel, collects events, owns navigation.
- `<Feature>ScreenContent.kt` — stateless content receiving `state` + `onAction`.
- Distinct, named UI elements (item rows, top bars, empty states) get their own file under `ui/` even when used once — see `features/itemlist/.../ui/`.

### Effects Must Not Live Inside Conditional Branches

`LaunchedEffect` / `DisposableEffect` placed inside an `if`/`when` branch are disposed and
recreated whenever state flips out of that branch. Keep effects at the top level of the Composable.

```kotlin
// ❌ WRONG
when (state) { is State.Success -> { LaunchedEffect(Unit) { onAction(Action.Track) }; Content(state) } }

// ✅ CORRECT
LaunchedEffect(Unit) { onAction(Action.Track) }
when (state) { is State.Success -> Content(state) }
```

### `remember` Only for Expensive Work

Use `remember(key)` for list construction, object allocation, or multi-branch derivations that
vary with state. Don't wrap cheap O(1) lookups in `remember` — the overhead isn't justified.

### Modifier Order

Place `.clip(shape)` before `.background(color)` — `.background()` without a preceding `.clip()`
paints outside the clipped boundary. Weights in a `Row`/`Column` must sum to `1f`.

### Lists

Use `LazyColumn` / `LazyRow` for scrolling lists, with stable `key`s.
