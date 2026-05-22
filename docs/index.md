# Droid Passwords — Documentation

A cross-platform password manager built with **Kotlin Multiplatform** and **Compose Multiplatform**, supporting **Android**, **iOS**, and **Desktop**. Passwords are stored locally inside an encrypted vault file.

This documentation is the entry point for both human contributors and AI agents. Each page is short, self-contained, and links back here. Start with [Overview](./overview.md) and [Architecture](./architecture.md), then drill into the layer or feature you care about.

---

## Quick Map

```
┌──────────────────────────────────────────────────────────────┐
│  composeApp        App entry, DI bootstrap, theming, nav     │
├──────────────────────────────────────────────────────────────┤
│  features/*        ViewModel + Reducers + ScreenRoot + UI    │
│  features-api/*    Public Screen contracts (Voyager)         │
├──────────────────────────────────────────────────────────────┤
│  data              Repositories, Vault file I/O, Crypto,     │
│                    DataStore, LockManager, ThemeManager      │
├──────────────────────────────────────────────────────────────┤
│  core              MVI infrastructure, shared UI, snackbar   │
└──────────────────────────────────────────────────────────────┘
```

---

## Project-wide reading

| Doc | What's inside |
| --- | --- |
| [Overview](./overview.md) | What the product does, supported platforms, build commands |
| [Architecture](./architecture.md) | MVI pattern, module layout, navigation model, DI wiring |
| [Core layer](./layer-core.md) | `MviViewModel`, `ActionProcessor`, `Reducer`, shared composables |
| [Data layer](./layer-data.md) | Vault model, file repository, runtime repository, crypto, settings, lock |
| [composeApp module](./layer-composeapp.md) | `App`, `AppContent`, theme, navigator root, platform entry points |
| [Adding a feature](./adding-a-feature.md) | Step-by-step guide for new screens |

## Feature modules

Each feature lives in two modules: `features-api/<name>` (public `Screen` contract) and `features/<name>` (implementation).

| Feature | Doc | Purpose |
| --- | --- | --- |
| `welcome` | [welcome](./feature-welcome.md) | First-launch entry; create new vault or open existing |
| `setlock` | [setlock](./feature-setlock.md) | Set, change, or remove the vault lock password |
| `unlock` | [unlock](./feature-unlock.md) | Prompt for the password and decrypt the vault |
| `itemlist` | [itemlist](./feature-itemlist.md) | Password list with search and filtering by tag |
| `itemdetails` | [itemdetails](./feature-itemdetails.md) | View / copy / share / edit / delete a single item |
| `edit` | [edit](./feature-edit.md) | Add or edit a password with additional fields and tags |
| `tags` | [tags](./feature-tags.md) | Browse and search tags |
| `settings` | [settings](./feature-settings.md) | Theme, lock interval, vault file path, change password |
| `export` | [export](./feature-export.md) | Export vault to CSV / JSON / encrypted DATA |
| `importdata` | [importdata](./feature-importdata.md) | Import vault from CSV / JSON / encrypted DATA |

---

## How to read this for an AI agent

1. Read **Overview** and **Architecture** first — they define vocabulary (State / Action / Event / Reducer / ScreenRoot) used everywhere else.
2. Read the **Core** and **Data** layer docs to understand the building blocks the features compose.
3. Open the feature doc that matches the change you're asked to make. Every feature doc follows the same layout, so you can skim quickly.
4. Use [Adding a feature](./adding-a-feature.md) when scaffolding a new screen.
