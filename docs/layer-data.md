# Data layer

← [Back to index](./index.md) · related: [Architecture](./architecture.md)

The `data` module owns *all* persistence and state-outside-the-UI. Features call into it via Koin-injected repositories and managers; they never touch files, DataStore, or crypto APIs directly.

## Vault model

The whole user database is one Kotlin data class tree, JSON-serialised and AES-encrypted on disk.

```kotlin
@Serializable
data class VaultModel(
    val password: String,           // re-stored inside the file as a safety check
    val items: List<VaultItemModel>
)

@Serializable
data class VaultItemModel(
    val id: String,
    val title: String,
    val username: String,
    val password: String,
    val tags: List<VaultItemTag> = emptyList(),
    val url: String? = null,
    val description: String? = null,
    val additionalFields: List<VaultAdditionalFieldModel> = emptyList()
)
```

Supporting models live next to those files in `data/vault/`:

- `VaultItemTag` — `{ id, name, color }` (or similar), used for grouping items.
- `VaultAdditionalFieldModel` — arbitrary key/value pairs the user attaches to an item.

`data/model/` also defines lightweight models exposed to the UI layer:

- `ItemModel` — UI-friendly mirror of `VaultItemModel`.
- `AdditionalFieldsModel`
- `LockInterval` — enum: `NEVER`, `ONE_MINUTE`, `FIVE_MINUTES`, … (with a `minutes` property used by `LockManager`).
- `ThemeMode` — `LIGHT`, `DARK`, `SYSTEM`.

## VaultFileRepository

`data/vault/VaultFileRepository.kt`. Stateless; encapsulates *every* read/write of the encrypted vault file.

| API | What it does |
| --- | --- |
| `isVaultExists(): Boolean` | Checks `droid-d4.data` in the platform cache dir. |
| `createVault(password): AppResult<Unit>` | Writes an empty, encrypted vault with the given password. |
| `getVault(password): AppResult<VaultModel>` | Decrypts the default vault file. Used at unlock time. |
| `getVaultFromSpecificFile(password, file)` | Same, but for an import file the user picked. |
| `saveVault(vault): AppResult<Unit>` | Re-encrypts and writes the default vault. Used after every edit. |
| `saveVaultToSpecificFile(vault, file)` | Used by export. |
| `changePassword(old, new): AppResult<Unit>` | Decrypts with `old`, re-encrypts with `new`. |

Internally it uses **FileKit** (`PlatformFile`, `FileKit.cacheDir`) for cross-platform file access, **kotlinx.serialization** for JSON, and the `CryptoMapper` for the encryption pass.

The file name is the constant `droid-d4.data` (see `DEFAULT_FILE_NAME`).

## VaultRuntimeRepository

`data/vault/VaultRuntimeRepository.kt`. The decrypted in-memory vault. After [unlock](./feature-unlock.md) decrypts the file, the result is `loadVault(...)`-ed here, and every read/write in the running session bounces off this object until the user explicitly persists via `VaultFileRepository.saveVault`.

| API | Notes |
| --- | --- |
| `loadVault(vault)` | Replace runtime contents. |
| `getVault(): VaultModel` | Read snapshot. |
| `addOrUpdateVault(item)` | Insert or replace by `id`. |
| `deleteVaultById(itemId)` | Remove an item. |
| `getAllTags(): List<VaultItemTag>` | Distinct flattened tags across all items. |

> The runtime repository does **not** persist anything. Features must call `VaultFileRepository.saveVault(...)` after mutating it.

## CryptoMapper

`data/vault/CryptoMapper.kt`. Wraps **cryptography-kotlin** to expose two suspending functions:

- `encode(password, plaintext): ByteArray`
- `decode(password, ciphertext): String`

Platform-specific crypto providers are wired in: JDK provider for Android/Desktop, Apple provider for iOS.

## PasswordRepository

`data/PasswordRepository.kt`. An `object` (singleton) backed by `mutableStateListOf<ItemModel>()`. It is the UI-facing list the password screens observe.

| API | Notes |
| --- | --- |
| `passwords: List<ItemModel>` | Snapshot. |
| `addPassword(item)` | Append. |
| `updatePassword(item)` | Replace by `id`. |
| `deletePassword(passwordId)` | Remove by `id`. |
| `getPasswordById(id)` | Lookup. |

> `PasswordRepository` holds *UI-level* `ItemModel` rows; `VaultRuntimeRepository` holds the source-of-truth `VaultItemModel`. Features keep them in sync (mappers in features such as `itemlist`).

## SettingsRepository

`data/SettingsRepository.kt` (interface) + `SettingsRepositoryImpl.kt` (DataStore-Preferences backed).

Persists:

- `lockInterval: LockInterval` — auto-lock timeout.
- `themeMode: ThemeMode`
- `vaultFilePath: String?` — where the vault is stored (settings let the user move it).

Used directly by `settings` and indirectly by `LockManager` / `ThemeManager`.

`createDataStore.<platform>.kt` provides the platform-specific path the DataStore writes to.

## LockManager

`data/LockManager.kt`. An `object` because the lock state is process-global. Holds:

- The currently set lock password (in memory).
- An `isLocked` flag.
- An `isFirstLaunch` flag.
- A `LockInterval` and a timer `Job`.

| API | Notes |
| --- | --- |
| `setLockPassword(password)` | Set initial password and mark as locked. |
| `validatePassword(password)` / `unlock(password)` | Validates and clears `isLocked` on success. Starts the auto-lock timer. |
| `lock()` | Re-locks immediately and cancels the timer. |
| `removeLockPassword()` | Clears the password and unlocks. |
| `changeLockPassword(old, new)` | After validating `old`. |
| `setLockInterval(interval)` / `resetLockTimer()` | Adjust or reset the inactivity timer. |
| `completeFirstLaunch()` | Called once after welcome flow. |

The auto-lock timer is a single `Job` on a `CoroutineScope(Dispatchers.Default + SupervisorJob())`; it calls `lock()` after `LockInterval.minutes * 60_000 ms`.

## ThemeManager

`data/ThemeManager.kt` (+ `<platform>` overrides). Exposes the user's selected `ThemeMode` as a `Flow` so `App.kt` can recompose on change. The platform overrides supply `isSystemInDarkMode()` for the `SYSTEM` mode.

## LocalStorage / PrefsManager

`data/PrefsManager.kt` and the `createLocalStorageDataStore.<platform>.kt` siblings provide a generic key/value DataStore wrapper used by `SettingsRepositoryImpl`.

## FileFormat

`data/FileFormat.kt`. Enum used by import/export: `CSV`, `JSON`, `DATA` (the encrypted native format).

## DI

`DataDi.kt` exposes `Module.registerDataDi()` and registers:

```kotlin
factoryOf(::LocalStorage)
single { LockManager }                 // already an object
factoryOf(::VaultFileRepository)
singleOf(::VaultRuntimeRepository)
singleOf(::CryptoMapper)
singleOf(::ThemeManager)
single { Json { ignoreUnknownKeys = true } }
```

`PasswordRepository` is an `object` and is used directly (no DI binding).
