---
paths:
  - "features/**/*.kt"
  - "features-api/**/*.kt"
  - "core/**/*.kt"
---

## MVI Architecture Rules

This project uses a small in-house MVI runtime in `core/src/commonMain/.../core/mvi/`
(`MviViewModel`, `ActionProcessor`, `Reducer`, `InitStateFactory`). To scaffold a new
feature, copy the simplest existing modules (`welcome`, `setlock`) — see `docs/adding-a-feature.md`.

### MVI Fundamentals

- **State** represents UI state — model it as a sealed interface with explicit `Loading` / `Error` / `Success` cases where relevant.
- **Actions** are user intents or system triggers the reducer reacts to.
- **Events** are one-time side effects (navigation, clipboard, snackbar).
- **Reducers** transform state in response to one action class.
- **Actions must NEVER contain lambda/function parameters** — they are data holders. Dispatch follow-up work through the reducer result instead.
  - ❌ `data class OnResume(val onAction: (Action) -> Unit)`
  - ✅ `data object OnResume : Action`

### Action vs Event — Direction of Flow

| Type | Direction | Purpose | Callback name |
|---|---|---|---|
| `Action` | **UI → ViewModel** | User intent (tap, input) or system trigger | `onAction: (Action) -> Unit` |
| `Event` | **ViewModel → UI** | One-shot side effect the UI performs (navigate, copy, snackbar) | collected via `viewModel.event.collect { }` |

A Composable parameter whose lambda emits something the user did is **always** `onAction`,
never `onEvent`. Reserve `Event` / `onEvent` strictly for one-shot ViewModel-emitted side
effects collected in the screen root.

### One Reducer Per Action Class

Each concrete action class gets exactly one `Reducer`. `ActionProcessor` throws
`"Reducers must have unique action classes"` at runtime if two reducers share an `actionClass`.
One file per reducer under `reducer/`.

```kotlin
class InitScreenReducer(
    private val runtimeRepository: VaultRuntimeRepository,   // repositories injected directly — see below
    private val vaultItemMapper: VaultItemMapper
) : Reducer<PasswordListScreenAction.InitScreen, PasswordListScreenState, PasswordListScreenAction, PasswordListScreenEvent> {
    override val actionClass = PasswordListScreenAction.InitScreen::class
    override suspend fun reduce(
        action: PasswordListScreenAction.InitScreen,
        getState: () -> PasswordListScreenState
    ): Reducer.Result<PasswordListScreenState, PasswordListScreenAction.InitScreen, PasswordListScreenEvent?> { ... }
}
```

### Chaining and Effects

- `reduce()` returns `Reducer.Result(state, action?, event?)`.
- A non-null `action` is processed immediately and atomically (e.g. `OnSaveClicked` → `OnSave`). Use this for short chains instead of side effects.
- A non-null `event` is a one-shot effect. The screen root collects it and performs the effect.

### Reducers Call Repositories in `data` Directly

This project has **no Use-Case layer**. Reducers inject and call repositories from the `data`
module (`VaultRuntimeRepository`, `SettingsRepository`, `PasswordRepository`, etc.) directly.
Suspending work (file I/O, encryption) happens inside `reduce()`.

What reducers must **not** do:
- Touch `kotlinx datastore`, FileKit, or crypto APIs directly — always go through a repository in `data`.
- Call the Voyager `Navigator` — emit a `Navigate*` event instead (see below).
- Perform UI side effects (clipboard, snackbar) — emit an event.

### getState() — Capture Once

`reduce()` is a `suspend` function and `getState()` reads a live `StateFlow`; two calls can
return different snapshots across a suspension point. Capture once at the top and derive
everything from that reference.

```kotlin
// ✅ CORRECT
val currentState = getState()
if (currentState is State.Success) { ... }
```

### Navigation via Event Only

Navigation flows through `Event`, collected in the **screen root** Composable, which translates
it into `navigator.push / pop / replace`. Reducers never reference `Navigator`.

```kotlin
LaunchedEffect(Unit) {
    viewModel.event.collect { event ->
        when (event) {
            is Event.NavigateToDetail -> navigator.push(PasswordDetailScreen(event.item))
            Event.NavigateBack -> navigator.pop()
        }
    }
}
```

### Cross-feature Navigation Contract

The public Voyager `Screen` + its `ScreenFactory` interface live in `features-api/<name>`.
A feature that navigates to another depends only on that feature's `features-api` module,
never on its implementation. Implementation never leaks across feature boundaries.

### State Rules

- **Immutable**: `data class` with `val` only — never `var` in State. All transitions go through reducers.
- **Single source of truth**: don't store a boolean that derives from existing data (e.g. `isEditMode` alongside `itemId != null`) — compute it.
- **One action per user interaction**: each distinct intent gets its own descriptively-named `Action` subtype. Don't overload one action with a `type` discriminator.
- **Pre-format in State**: build display strings (titles, labels) in the reducer; Composables render ready values, no formatting in the UI.

### Sealed Types Live in `model/`

`State`, `Action`, `Event`, and domain sealed types belong in their own files under the feature's
`model/` package — never defined inside a reducer or UI file.

### Dependency Injection

Each feature exposes one `fun Module.register<Feature>ScreenDi()` registering the ViewModel,
`ActionProcessor`, every reducer, `InitStateFactory`, mappers, and the `ScreenFactory`. It must
be called from `composeApp/.../di/appDi.kt`.

- **Use `factoryOf()` / `singleOf()` shorthand.** Don't write verbose `factory { X(get(), get()) }`.
- **Exception — `ActionProcessor`**: `factoryOf()` can't construct the `Set<Reducer<…>>`, so the processor is registered with an explicit `factory { }` that builds the `reducers = setOf(get(...), ...)`. This is the only accepted `factory {}`.

```kotlin
private fun Module.reducersDi() {
    factoryOf(::InitScreenReducer)
    factoryOf(::OnSearchChangedReducer)
    factory {
        PasswordListScreenActionProcessor(
            reducers = setOf(get(InitScreenReducer::class), get(OnSearchChangedReducer::class)),
            initStateFactory = get(),
            coroutineContextProvider = get()
        )
    }
}
```

### No Logic in init Blocks

`init` blocks must not contain business logic or object instantiation. Use an explicit method.
