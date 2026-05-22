# Overview

← [Back to index](./index.md)

## What it is

**Droid Passwords** is a local-first password manager. Everything lives in a single encrypted file on the user's device — there is no server, no cloud sync, and no account.

The same Kotlin Multiplatform codebase targets:

- **Android** (`androidMain`)
- **iOS** (`iosMain`)
- **Desktop** — macOS / Windows / Linux (`desktopMain`)

UI is rendered with **Compose Multiplatform**, navigation is handled by **Voyager**, dependency injection by **Koin**, and encryption by **cryptography-kotlin (CryptoKit)**.

## What the user can do

| Capability | Where it lives |
| --- | --- |
| Create or open a vault file | [welcome](./feature-welcome.md), [unlock](./feature-unlock.md) |
| Set / change / remove the vault password | [setlock](./feature-setlock.md), [settings](./feature-settings.md) |
| Add, edit, delete password items (title, username, password, URL, description, custom fields, tags) | [edit](./feature-edit.md), [itemdetails](./feature-itemdetails.md) |
| Browse the password list with search | [itemlist](./feature-itemlist.md) |
| Group items by tag | [tags](./feature-tags.md), [itemlist](./feature-itemlist.md) |
| Copy / share a password field | [itemdetails](./feature-itemdetails.md) |
| Auto-lock after an interval | [Lock manager](./layer-data.md#lockmanager), [settings](./feature-settings.md) |
| Theme: light / dark / system | [Theme manager](./layer-data.md#thememanager), [settings](./feature-settings.md) |
| Import / export to CSV, JSON, or encrypted DATA | [importdata](./feature-importdata.md), [export](./feature-export.md) |

## Build & run

| Target | Command |
| --- | --- |
| Android install | `./gradlew :composeApp:installDebug` |
| Android APK | `./gradlew :composeApp:assembleDebug` |
| Desktop run | `./gradlew :composeApp:run` |
| Desktop package | `./gradlew :composeApp:packageReleaseDmg` (macOS), `packageReleaseMsi` (Windows), `packageReleaseDeb` (Linux) |
| Build everything | `./gradlew build` |
| Clean | `./gradlew clean` |

iOS is built via `iosApp/` (Xcode project) which embeds the shared Kotlin framework.

## Storage at a glance

- **Vault file**: a single binary file named `droid-d4.data` placed in the platform cache directory by `FileKit`. Contents are JSON, encrypted with the user's password by `CryptoMapper`. See [Data layer](./layer-data.md).
- **Settings**: `androidx.datastore` Preferences — stores theme mode, lock interval, vault file path.
- **Runtime vault**: the decrypted vault lives in memory in `VaultRuntimeRepository` while the app is unlocked.

## Reading the rest

If you understand the [MVI pattern](./architecture.md#mvi-flow) and the [vault model](./layer-data.md#vault-model), every feature doc reads the same way. Pick one and go.
