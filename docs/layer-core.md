# Core layer

← [Back to index](./index.md) · related: [Architecture](./architecture.md)

The `core` module hosts the MVI runtime plus a handful of UI primitives shared across every feature. It has no dependency on `data` and depends on nothing application-specific.

## MVI runtime (`core/mvi/`)

| File | Role |
| --- | --- |
| `MviViewModel.kt` | `androidx.lifecycle.ViewModel` that holds an `ActionProcessor` and forwards `onAction` calls into `viewModelScope.launch`. Exposes `state: StateFlow` and `event: Flow`. |
| `ActionProcessor.kt` | Owns the `MutableStateFlow<STATE>`, the event `Channel<EVENT>`, and a `Set<Reducer>`. Runs reducer dispatch on `coroutineDispatcher.limitedParallelism(1)` so reducers are strictly serial. Looks up the reducer whose `actionClass` matches the current action, applies the result (`state`, optional follow-up `action`, optional `event`), then recurses if there is a follow-up. |
| `Reducer.kt` | Interface a feature implements per action. Declares `val actionClass: KClass<ACTION>` so the processor can match it. `reduce(action, getState)` returns `Reducer.Result(state, action?, event?)`. |
| `InitStateFactory.kt` | Pluggable factory that produces the initial state — useful for screens whose initial state depends on constructor arguments (e.g. `PasswordDetailScreen(item)`). |
| `CoroutineContextProvider.kt` | Supplies the `CoroutineDispatcher` to `ActionProcessor` so tests can swap in a deterministic one. |
| `AppResult.kt` | Tiny sealed result wrapper (`Success(T)` / `Failure(Throwable)`) used throughout the data layer instead of exceptions-as-control-flow. |

### Why limitedParallelism(1)?

So a stream of actions on the same screen mutates state in the order they arrived. The processor implementation:

```kotlin
suspend fun process(action: ACTION) = withContext(dispatcher) {
    val result = internalProcess(action)
    _state.update { result.state }
    result.event?.let { _event.send(it) }
    result.action?.let { process(it) }  // chained action runs synchronously next
}
```

A chained `action` is processed inside the same `withContext`, so a reducer can model multi-step transitions atomically (e.g. `OnSaveClicked` → `OnSave`).

## Shared UI (`core/ui/`)

| File | Role |
| --- | --- |
| `AppSnackbarHost.kt` | A Composable host installed once in `AppContent` so every screen shares one snackbar queue. |
| `SnackbarHostStateManager.kt` | Holds the `SnackbarHostState` and exposes a coroutine-friendly API features call to show messages. |
| `EOutlinedTextField.kt` | Wrapper around `OutlinedTextField` that adds consistent validation/error styling used in every form (edit, settings, unlock, import). |
| `TextValidator.kt` | Small validation primitives used by the form fields above. |

## Navigation helpers (`core/navigation/`)

| File | Role |
| --- | --- |
| `LocalWideScreenFlag.kt` | `CompositionLocal<Boolean>` set by `AppContent` when the window is ≥ 600 dp wide. Features read it to render two-pane layouts on desktop / large tablets. |

## DI

`CoreDi.kt` exposes `Module.registerCoreDi()`. It registers:

- The `CoroutineContextProvider` and a default `CoroutineDispatcher`.
- The shared `SnackbarHostStateManager`.

Every other module assumes those are already available.
