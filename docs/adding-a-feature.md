# Adding a new feature

← [Back to index](./index.md) · related: [Architecture](./architecture.md)

This is the recipe for scaffolding a brand-new screen. Use the existing feature modules as templates — `welcome` and `setlock` are the simplest.

## 1. Create the API module

`features-api/<myfeature>/` — its only job is to expose the public `Screen` so other features can navigate to it.

```kotlin
// features-api/myfeature/.../navigation/MyFeatureScreen.kt
data object MyFeatureScreen : Screen {
    @Composable
    override fun Content() {
        MyFeatureScreenRoot()      // ScreenRoot is in features/myfeature
    }
}

// features-api/myfeature/.../navigation/MyFeatureScreenFactory.kt
interface MyFeatureScreenFactory {
    fun create(/* params */): MyFeatureScreen
}
```

Add the module in `settings.gradle.kts`:

```kotlin
include(":features-api:myfeature")
```

## 2. Create the implementation module

`features/<myfeature>/` — implementation lives here.

```
features/myfeature/
  MyFeatureScreenRoot.kt
  MyFeatureViewModel.kt
  MyFeatureActionProcessor.kt
  model/
    MyFeatureScreenState.kt
    MyFeatureScreenAction.kt
    MyFeatureScreenEvent.kt
  reducer/
    InitScreenReducer.kt
    OnSomethingClickedReducer.kt
  ui/
    SomeSubComposable.kt
  MyFeatureDi.kt
```

Add to `settings.gradle.kts`:

```kotlin
include(":features:myfeature")
```

## 3. Wire model, ViewModel, processor

```kotlin
sealed interface MyFeatureScreenState {
    data object Loading : MyFeatureScreenState
    data class Success(val ...) : MyFeatureScreenState
}

sealed interface MyFeatureScreenAction {
    data object InitScreen : MyFeatureScreenAction
    data class OnSomethingClicked(val id: String) : MyFeatureScreenAction
}

sealed interface MyFeatureScreenEvent {
    data object NavigateBack : MyFeatureScreenEvent
}
```

```kotlin
class MyFeatureViewModel(
    processor: MyFeatureActionProcessor
) : MviViewModel<MyFeatureScreenState, MyFeatureScreenAction, MyFeatureScreenEvent>(processor)

class MyFeatureActionProcessor(
    reducers: Set<Reducer<MyFeatureScreenAction, MyFeatureScreenState, MyFeatureScreenAction, MyFeatureScreenEvent>>,
    init: InitStateFactory<MyFeatureScreenState>,
    contextProvider: CoroutineContextProvider
) : ActionProcessor<...>(reducers, init, contextProvider.default)
```

## 4. Reducers — one file per action

```kotlin
class InitScreenReducer(
    private val repo: SomeRepository
) : Reducer<MyFeatureScreenAction.InitScreen, MyFeatureScreenState, MyFeatureScreenAction, MyFeatureScreenEvent> {
    override val actionClass = MyFeatureScreenAction.InitScreen::class
    override suspend fun reduce(
        action: MyFeatureScreenAction.InitScreen,
        getState: () -> MyFeatureScreenState
    ): Reducer.Result<MyFeatureScreenState, MyFeatureScreenAction, MyFeatureScreenEvent?> {
        val data = repo.load()
        return Reducer.Result(
            state = MyFeatureScreenState.Success(data),
            action = null,
            event = null
        )
    }
}
```

Rules:

- One `Reducer` class per concrete action class. `ActionProcessor` will throw if two reducers share an `actionClass`.
- Use `action = SomeFollowUp` to chain (e.g. `OnSaveClicked` → `OnSave`). The chain runs atomically.
- Use `event = NavigateBack` for one-shot effects. Don't model navigation inside state.

## 5. Screen root

```kotlin
@Composable
fun MyFeatureScreenRoot() {
    val viewModel: MyFeatureViewModel = koinInject()
    val state by viewModel.state.collectAsState()
    val navigator = LocalNavigator.currentOrThrow

    LaunchedEffect(Unit) {
        viewModel.onAction(MyFeatureScreenAction.InitScreen)
        viewModel.event.collect { event ->
            when (event) {
                MyFeatureScreenEvent.NavigateBack -> navigator.pop()
            }
        }
    }

    MyFeatureContent(
        state = state,
        onAction = viewModel::onAction
    )
}
```

Keep navigation, copy-to-clipboard, snackbar and other side effects inside the event collector — *not* inside reducers.

## 6. DI

```kotlin
fun Module.registerMyFeatureScreenDi() {
    factoryOf(::MyFeatureViewModel)
    factoryOf(::MyFeatureActionProcessor) bind <something if needed>
    factoryOf(::MyFeatureInitStateFactory) bind InitStateFactory::class

    factoryOf(::InitScreenReducer) bind Reducer::class
    factoryOf(::OnSomethingClickedReducer) bind Reducer::class
    // … one binding per reducer
}
```

Then in `composeApp/.../di/appDi.kt`:

```kotlin
val appModule: Module = module {
    // …existing…
    registerMyFeatureScreenDi()
}
```

## 7. Build wiring

In `composeApp/build.gradle.kts` add the two dependencies:

```kotlin
implementation(project(":features:myfeature"))
implementation(project(":features-api:myfeature"))
```

And in any feature module that needs to navigate to yours:

```kotlin
implementation(project(":features-api:myfeature"))
```

## 8. Pattern checklist

Before opening a PR, verify:

- [ ] Public `Screen` lives in `features-api/<name>`, implementation does not leak.
- [ ] One reducer per action class; chained actions use `Reducer.Result.action`.
- [ ] All side effects (navigation, clipboard, snackbar, share) go through `EVENT`.
- [ ] State has explicit `Loading` and `Error` cases when relevant.
- [ ] `register<Feature>ScreenDi()` is called in `appDi.kt`.
- [ ] No direct file/datastore/crypto access in features — all goes through repositories in `data`.
