---
name: create-feature-modules
description: Scaffolds a complete feature for Droid Passwords — the features-api/<name> (public Voyager Screen + Factory) and features/<name> (MVI implementation: state/action/event, reducers, ViewModel, ActionProcessor, DI, UI) plus Gradle and Koin wiring. Use when starting a new screen from scratch.
args: one or more feature names, lowercase no spaces (e.g. "notes" or "notes sharing")
---

You are scaffolding one or more feature modules for **Droid Passwords**, a Kotlin Multiplatform +
Compose Multiplatform app using an in-house MVI runtime. Read `docs/adding-a-feature.md` and
`.claude/rules/mvi-architecture.md` first — they are the authoritative spec. **Use the `welcome`
feature (`features/welcome`, `features-api/welcome`) as the reference implementation** — it is the
simplest complete example.

## Feature names to create: {{args}}

## Placeholders

- `<name>` → module dir + package segment, lowercase no dashes (e.g. `notes`)
- `<Feature>` → PascalCase class prefix (e.g. `Notes`). May differ from `<name>` if the screen has a
  domain name (e.g. module `itemlist` uses prefix `PasswordList`). Ask the user if ambiguous.

Package base is `io.github.devapro.droid.<name>` for both modules (mirror `welcome`). Source set is
`commonMain` (this is KMP — not `src/main/kotlin`). Tests go in `commonTest`.

## What to create per feature

### features-api/<name>/  (public navigation contract only — 3 files)

`build.gradle.kts` — copy `features-api/welcome/build.gradle.kts` verbatim, changing only
`android { namespace = "io.github.devapro.droid.<name>" }`.

`src/androidMain/AndroidManifest.xml` — copy `features-api/welcome`'s (a bare `<manifest>` tag).

`src/commonMain/.../<name>/navigation/<Feature>Screen.kt`:
```kotlin
package io.github.devapro.droid.<name>.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.github.devapro.droid.<name>.<Feature>ScreenFactory
import org.koin.compose.koinInject

object <Feature>Screen : Screen {
    @Composable
    override fun Content() {
        val factory: <Feature>ScreenFactory = koinInject()
        factory.Create<Feature>Screen()
    }
}
```
> If the screen needs navigation arguments, make it a `data class` with `val` params instead of an
> `object`, and thread them through `Create<Feature>Screen(...)`.

`src/commonMain/.../<name>/<Feature>ScreenFactory.kt`:
```kotlin
package io.github.devapro.droid.<name>

import androidx.compose.runtime.Composable

interface <Feature>ScreenFactory {
    @Composable
    fun Create<Feature>Screen()
}
```

### features/<name>/  (implementation)

`build.gradle.kts` — copy `features/welcome/build.gradle.kts`, change `namespace`, and fix the
`commonMain.dependencies` block to declare only what this feature uses:
`implementation(projects.core)`, `implementation(projects.data)`,
`implementation(projects.featuresApi.<name>)`, plus the `projects.featuresApi.*` of any other screen
it navigates to.

`src/androidMain/AndroidManifest.xml` — copy welcome's.

`src/commonMain/.../<name>/model/<Feature>ScreenState.kt`:
```kotlin
package io.github.devapro.droid.<name>.model

sealed interface <Feature>ScreenState {
    data object Loading : <Feature>ScreenState
    data class Error(val message: String) : <Feature>ScreenState
    data class Success(/* pre-formatted, immutable val fields */) : <Feature>ScreenState
}
```

`model/<Feature>ScreenAction.kt`:
```kotlin
package io.github.devapro.droid.<name>.model

sealed interface <Feature>ScreenAction {
    data object InitScreen : <Feature>ScreenAction
    // one distinct, descriptively-named subtype per user interaction
}
```

`model/<Feature>ScreenEvent.kt`:
```kotlin
package io.github.devapro.droid.<name>.model

sealed interface <Feature>ScreenEvent {
    data object NavigateBack : <Feature>ScreenEvent
    // one-shot effects only (navigation, clipboard, snackbar)
}
```

`factory/<Feature>ScreenInitStateFactory.kt`:
```kotlin
package io.github.devapro.droid.<name>.factory

import io.github.devapro.droid.core.mvi.InitStateFactory
import io.github.devapro.droid.<name>.model.<Feature>ScreenState

class <Feature>ScreenInitStateFactory : InitStateFactory<<Feature>ScreenState> {
    override fun createInitState(): <Feature>ScreenState = <Feature>ScreenState.Loading
}
```

`reducer/InitScreenReducer.kt` — one reducer per action class. Inject `data` repositories directly:
```kotlin
package io.github.devapro.droid.<name>.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.<name>.model.<Feature>ScreenAction
import io.github.devapro.droid.<name>.model.<Feature>ScreenEvent
import io.github.devapro.droid.<name>.model.<Feature>ScreenState

class InitScreenReducer(
    // private val runtimeRepository: VaultRuntimeRepository,  // inject data repos as needed
) : Reducer<<Feature>ScreenAction.InitScreen, <Feature>ScreenState, <Feature>ScreenAction, <Feature>ScreenEvent> {
    override val actionClass = <Feature>ScreenAction.InitScreen::class
    override suspend fun reduce(
        action: <Feature>ScreenAction.InitScreen,
        getState: () -> <Feature>ScreenState
    ): Reducer.Result<<Feature>ScreenState, <Feature>ScreenAction.InitScreen, <Feature>ScreenEvent?> {
        return Reducer.Result(
            state = <Feature>ScreenState.Success(/* ... */),
            action = null,
            event = null
        )
    }
}
```

`<Feature>ScreenActionProcessor.kt`:
```kotlin
package io.github.devapro.droid.<name>

import io.github.devapro.droid.core.mvi.ActionProcessor
import io.github.devapro.droid.core.mvi.CoroutineContextProvider
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.<name>.factory.<Feature>ScreenInitStateFactory
import io.github.devapro.droid.<name>.model.<Feature>ScreenAction
import io.github.devapro.droid.<name>.model.<Feature>ScreenEvent
import io.github.devapro.droid.<name>.model.<Feature>ScreenState

class <Feature>ScreenActionProcessor(
    private val initStateFactory: <Feature>ScreenInitStateFactory,
    private val coroutineContextProvider: CoroutineContextProvider,
    reducers: Set<Reducer<<Feature>ScreenAction, <Feature>ScreenState, <Feature>ScreenAction, <Feature>ScreenEvent>>,
) : ActionProcessor<<Feature>ScreenState, <Feature>ScreenAction, <Feature>ScreenEvent>(
    reducers = reducers,
    initStateFactory = initStateFactory,
    coroutineDispatcher = coroutineContextProvider.default
)
```

`<Feature>ScreenViewModel.kt`:
```kotlin
package io.github.devapro.droid.<name>

import io.github.devapro.droid.core.mvi.MviViewModel
import io.github.devapro.droid.<name>.model.<Feature>ScreenAction
import io.github.devapro.droid.<name>.model.<Feature>ScreenEvent
import io.github.devapro.droid.<name>.model.<Feature>ScreenState

class <Feature>ScreenViewModel(
    actionProcessor: <Feature>ScreenActionProcessor
) : MviViewModel<<Feature>ScreenState, <Feature>ScreenAction, <Feature>ScreenEvent>(actionProcessor)
```

`factory/<Feature>ScreenFactoryImpl.kt`:
```kotlin
package io.github.devapro.droid.<name>.factory

import androidx.compose.runtime.Composable
import io.github.devapro.droid.<name>.<Feature>ScreenFactory
import io.github.devapro.droid.<name>.<Feature>ScreenRoot

class <Feature>ScreenFactoryImpl : <Feature>ScreenFactory {
    @Composable
    override fun Create<Feature>Screen() {
        <Feature>ScreenRoot()
    }
}
```

`<Feature>ScreenRoot.kt` — resolves the ViewModel via `koinInject()`, fires `InitScreen`, collects
events and translates them to `navigator.push/pop/replace`, and renders `<Feature>ScreenContent`.
Model this on `features/welcome/.../WelcomeScreenRoot.kt` (or `itemlist`'s root for the
event-handler pattern). Navigation is done here with `LocalNavigator.currentOrThrow` — never in a
reducer.

`ui/<Feature>ScreenContent.kt` — stateless `@Composable fun <Feature>ScreenContent(state: ..., onAction: (<Feature>ScreenAction) -> Unit)`. No business logic, no `koinInject`. Split distinct UI
elements into their own files under `ui/`.

`<Feature>ScreenDi.kt`:
```kotlin
package io.github.devapro.droid.<name>

import io.github.devapro.droid.<name>.factory.<Feature>ScreenFactoryImpl
import io.github.devapro.droid.<name>.factory.<Feature>ScreenInitStateFactory
import io.github.devapro.droid.<name>.reducer.InitScreenReducer
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind

fun Module.register<Feature>ScreenDi() {
    factoryOf(::<Feature>ScreenViewModel)
    factoryOf(::<Feature>ScreenInitStateFactory)
    factoryOf(::<Feature>ScreenFactoryImpl).bind(<Feature>ScreenFactory::class)
    reducersDi()
}

private fun Module.reducersDi() {
    factoryOf(::InitScreenReducer)
    // factoryOf(::OnSomethingReducer)

    factory {  // ActionProcessor is the ONLY accepted factory{} — it builds the reducers Set
        <Feature>ScreenActionProcessor(
            reducers = setOf(
                get(InitScreenReducer::class),
            ),
            initStateFactory = get(),
            coroutineContextProvider = get()
        )
    }
}
```

### Optional: tests (commonTest)

Add `src/commonTest/.../reducer/InitScreenReducerTest.kt` using `kotlin.test` only (no MockK/Truth —
KMP). Assert `reducer.actionClass`, and compare the whole `Reducer.Result` against a `val expected`.
Use hand-written fakes for any `data` repository.

## Wiring (do this after creating files)

1. **`settings.gradle.kts`** — add:
   ```kotlin
   include(":features:<name>")
   include(":features-api:<name>")
   ```

2. **`composeApp/build.gradle.kts`** — in `commonMain.dependencies` add:
   ```kotlin
   implementation(projects.features.<name>)
   ```
   (The feature module declares its own `projects.featuresApi.<name>` dependency.)

3. **`composeApp/src/commonMain/.../di/appDi.kt`** — add `register<Feature>ScreenDi()` to the
   `appModule` block.

4. **Navigate to it** — from another feature's reducer emit a `Navigate*` event, then in that
   feature's screen root `navigator.push(<Feature>Screen)`. Add `projects.featuresApi.<name>` to
   that feature's `build.gradle.kts`.

## Verify

```bash
./gradlew :features-api:<name>:assemble
./gradlew :features:<name>:assemble
./gradlew :features:<name>:allTests   # if tests were added
```

## Execution

1. Use TaskCreate to track one task per feature.
2. For each feature, create the api module, then the feature module (model → factory → reducers →
   ActionProcessor → ViewModel → FactoryImpl → Root → Content → DI), then optional tests.
3. Do the four wiring steps.
4. Build to verify, then summarize what was created and how to navigate to the screen.

## Key reminders

- `commonMain` source set, package `io.github.devapro.droid.<name>` — NOT `src/main/kotlin`.
- One reducer per action class (the processor throws on duplicate `actionClass`).
- Reducers may call `data` repositories directly — there is no use-case layer — but never touch
  DataStore/FileKit/crypto directly, and never reference `Navigator`.
- `factoryOf`/`singleOf` everywhere except the `ActionProcessor` `factory {}`.
- Top-level `private const val`, never companion objects; explicit imports, no wildcards.
- Sealed `State`/`Action`/`Event` in `model/`; State immutable (`val` only).
