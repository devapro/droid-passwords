# composeApp module

← [Back to index](./index.md) · related: [Architecture](./architecture.md)

`composeApp` is the only module that imports every feature. It assembles the dependency graph and provides the platform entry points.

## Entry points

| Platform | File | Notes |
| --- | --- | --- |
| Android | `androidMain/.../DroidPasswordApplication.kt` | `Application` subclass that calls `initKoin()`. |
| Android | `androidMain/.../MainActivity.kt` | Hosts the `App()` composable. |
| Desktop | `desktopMain/.../main.kt` | `application { Window { App() } }` entry point. |
| Desktop | `desktopMain/.../ui/CustomTitleBar.kt`, `CustomWindowFrame.kt` | Custom OS chrome for the desktop build. |
| iOS | `iosMain/.../MainViewController.kt` | `ComposeUIViewController { App() }` exposed to Swift. |

There is also a `Platform.<platform>.kt` per source set returning a `Platform.name` string — purely informational.

## App composition

### `App.kt`

```kotlin
@Composable
fun App() {
    val themeManager: ThemeManager = koinInject()
    val themeMode by themeManager.getThemeMode().collectAsState(initial = ThemeMode.SYSTEM)
    val isDarkTheme = when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }
    MaterialTheme(colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()) {
        AppContent()
    }
}
```

- Subscribes to the user's chosen `ThemeMode` and re-evaluates the Material colour scheme reactively.
- Delegates rendering to `AppContent`.

### `AppContent.kt`

```kotlin
@Composable
fun AppContent() {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val isWideScreen = maxWidth >= 600.dp
        CompositionLocalProvider(LocalWideScreenFlag provides isWideScreen) {
            Scaffold(
                snackbarHost = { AppSnackbarHost() },
                content = {
                    Navigator(WelcomeScreen) { navigator -> SlideTransition(navigator) }
                }
            )
        }
    }
}
```

- Measures the window to set `LocalWideScreenFlag` (features use it for responsive layout).
- Installs the global `AppSnackbarHost` once.
- Creates the Voyager `Navigator` rooted at `WelcomeScreen` and wraps it with `SlideTransition` for animated push/pop.

## DI bootstrap

`composeApp/src/commonMain/kotlin/io/github/devapro/di/appDi.kt`:

```kotlin
fun initKoin() = startKoin { modules(appModule) }

val appModule: Module = module {
    registerCoreDi()
    registerDataDi()

    registerWelcomeScreenDi()
    registerImportScreenDi()
    registerExportScreenDi()
    registerSetLockPasswordScreenDi()
    registerUnLockVaultScreenDi()
    registerPasswordListScreenDi()
    registerPasswordDetailScreenDi()
    registerAddEditPasswordScreenDi()
    registerSettingsScreenDi()
    registerTagsScreenDi()
}
```

Every feature registers its own ViewModel, reducers, `ActionProcessor`, `InitStateFactory`, and `ScreenFactory`. `initKoin()` is called from the platform entry point (`Application.onCreate` on Android, `main()` on Desktop, `MainViewController` on iOS).

## Starting screen flow

```
WelcomeScreen
   │
   ├── no vault yet ──► SetLockPasswordScreen ──► (create vault) ──► PasswordListScreen
   │
   └── vault exists ──► UnLockVaultScreen ──► (decrypt + load runtime) ──► PasswordListScreen
```

From `PasswordListScreen` the user can branch to `TagsScreen`, `AddEditPasswordScreen`, `PasswordDetailScreen`, `SettingsScreen`, `ExportScreen`, or `ImportScreen`.
