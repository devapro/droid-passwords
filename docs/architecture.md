# Architecture

← [Back to index](./index.md)

This page explains the patterns every part of the app follows. Read this once and the feature docs become very short.

## Module layout

```
composeApp/         app entry, theming, Navigator root, Koin bootstrap
core/               MVI infrastructure + shared UI primitives
data/               repositories, vault, crypto, datastore, lock, theme
features/<name>/    feature implementation (ViewModel + reducers + UI)
features-api/<name>/ public Screen contract that other features can navigate to
iosApp/             Xcode shell that embeds the shared framework
```

Dependency direction:

```
composeApp ──► features/*          ──► features-api/*
                  │                       │
                  ▼                       ▼
                 data ────────────────► core
```

- `composeApp` is the only module that knows every feature exists. It assembles the Koin graph.
- A `features/<name>` module depends on its own `features-api/<name>` plus the `features-api` of any other screen it navigates to.
- `features-api/<name>` contains *only* the `Screen` data class / object and any model that's part of its navigation contract. This keeps cross-feature edges narrow.
- All modules can depend on `core` and `data`.

The full module list is in `settings.gradle.kts`.

## MVI flow

The codebase implements a small in-house MVI runtime in `core/mvi/`:

```
                   ┌──────────────┐
                   │   ScreenRoot │  (Composable: collects state, sends actions)
                   └──────┬───────┘
                          │ onAction(ACTION)
                          ▼
                  ┌────────────────┐
                  │  MviViewModel  │  exposes state: StateFlow, event: Flow
                  └──────┬─────────┘
                         │
                         ▼
                ┌────────────────────┐
                │  ActionProcessor   │  routes by action class
                └──────┬─────────────┘
                       │
                       ▼
                  ┌──────────┐
                  │ Reducer  │  pure: (action, getState) -> Result(state, nextAction?, event?)
                  └────┬─────┘
                       │ may call
                       ▼
                  ┌──────────┐
                  │ Repository│
                  └──────────┘
```

Key types (all in `io.github.devapro.droid.core.mvi`):

- **`MviViewModel<STATE, ACTION, EVENT>`** — exposes `state`, `event`, and `onAction(action)`. Pure delegation to `ActionProcessor`.
- **`ActionProcessor`** — holds the set of reducers, the `MutableStateFlow` for state, and an event `Channel`. Uses a single-parallelism dispatcher so reducers run sequentially. Routes incoming actions to the reducer whose `actionClass` matches.
- **`Reducer<ACTION, STATE, NEXT, EVENT>`** — one action class per reducer. `reduce(action, getState)` returns a `Result(state, action?, event?)`. A non-null `action` is processed immediately, enabling small chains.
- **`InitStateFactory<STATE>`** — produces the initial `STATE` so it can be DI-resolved.

This setup has a few practical consequences:

- **Reducers must each handle a unique action class.** The processor throws if duplicates exist.
- **Reducers can chain actions** (e.g. `OnSaveClicked` returns `action = OnSave` so the chain stays in the processor and remains atomic).
- **One-time effects** (navigation, copy-to-clipboard, snackbar) go through `EVENT`, not `STATE`. The `ScreenRoot` collects events in a `LaunchedEffect`.

See [Core layer](./layer-core.md) for the file-level details.

## Per-feature anatomy

Every feature folder follows the same shape:

```
features-api/<name>/
  navigation/
    <Feature>Screen.kt           # Voyager Screen (object or data class)
    <Feature>ScreenFactory.kt    # interface used to instantiate the screen
features/<name>/
  <Feature>ScreenRoot.kt         # Composable wiring ViewModel + UI + navigator
  <Feature>ViewModel.kt          # extends MviViewModel
  <Feature>ActionProcessor.kt    # extends ActionProcessor
  model/
    <Feature>ScreenState.kt      # sealed: Loading | Error | Success/Loaded
    <Feature>ScreenAction.kt     # sealed
    <Feature>ScreenEvent.kt      # sealed
  reducer/
    Init<…>Reducer.kt            # one file per action
    On<Thing>ClickedReducer.kt
    …
  ui/                            # private composables that make up the screen
  <Feature>Di.kt                 # fun Module.register<Feature>ScreenDi()
```

The `Factory` interface in `features-api` is the trick that lets one feature push another's screen without depending on the implementation module. Implementations of factories live next to the screen they create and are registered in DI.

## Navigation

Voyager is the navigation library. Conventions:

- `SlideTransition(navigator)` is applied once at the root in `AppContent.kt`.
- The root screen is `WelcomeScreen` — it decides whether to forward to `SetLockPasswordScreen` (no vault yet) or `UnLockVaultScreen` (vault exists).
- Reducers never call `Navigator` directly. They emit a `*Event` (`NavigateTo…`, `NavigateBack`, etc.) and the screen root translates that into `navigator.push / pop / replace` inside a `LaunchedEffect`.

## Dependency injection

Koin. Bootstrapped in `composeApp/.../di/appDi.kt`:

```kotlin
val appModule: Module = module {
    registerCoreDi()
    registerDataDi()
    registerWelcomeScreenDi()
    registerImportScreenDi()
    // … one register*() per feature
}
```

Every feature exposes a single `Module.register<Feature>ScreenDi()` extension that registers its `ViewModel`, `ActionProcessor`, every reducer, the `InitStateFactory`, and the `*ScreenFactory`. Composables resolve their ViewModel via `koinInject()`.

## Threading

- The Compose UI thread drives `ScreenRoot`.
- `ActionProcessor` runs reducers on a `CoroutineDispatcher` (`Dispatchers.Default` typically) with `limitedParallelism(1)` — reducer execution is strictly serial inside one screen.
- Suspending work (file I/O, encryption) happens inside the reducer.

## Platform code

The data layer is the only place with platform-specific source sets — see the `androidMain` / `iosMain` / `desktopMain` / `nativeMain` / `appleMain` folders under `data/src/`. Each defines its own:

- `createDataStore.<platform>.kt` — where DataStore persists settings.
- `createLocalStorageDataStore.<platform>.kt` — where the per-user storage lives.
- `ThemeManager.<platform>.kt` — how to read the system dark-mode flag.

`composeApp` has `Platform.<platform>.kt` files for trivial identification strings and small platform glue (`MainActivity`, `main.kt` for desktop, `MainViewController` for iOS).
