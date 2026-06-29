---
paths:
  - "**/*.kt"
---

## Coding Style

### Constants — Top-Level, Never Companion Objects

Define constants as `private const val` at the top of the file (after imports), not inside a
`companion object`.

```kotlin
// ❌ WRONG
class VaultFileRepository {
    companion object { private const val FILE_NAME = "droid-d4.data" }
}

// ✅ CORRECT
private const val FILE_NAME = "droid-d4.data"

class VaultFileRepository { ... }
```

Leave a blank line between top-level constants and the following declaration. When a constant is
used in more than one file, extract it as a top-level `const val` in the `model/` package
alongside the related type — never duplicate it.

### Named Arguments

Use named arguments for every call with 2+ parameters. Mixed named/positional is also a
violation. Exception: obvious stdlib calls (`listOf`, `mapOf`, `setOf`).

```kotlin
// ❌ WRONG
Reducer.Result(State.Success(items), null, null)

// ✅ CORRECT
Reducer.Result(state = State.Success(passwords = items), action = null, event = null)
```

### Coroutine Dispatchers — Inject CoroutineContextProvider

Never use `Dispatchers.IO` / `.Main` / `.Default` or `withContext(Dispatchers.X)` directly.
Inject `CoroutineContextProvider` (from `core/mvi`) and use `.io`, `.main`, `.default`. This
keeps code testable by substituting the provider.

```kotlin
// ❌ WRONG
withContext(Dispatchers.IO) { ... }

// ✅ CORRECT
class MyRepository(private val coroutineContextProvider: CoroutineContextProvider) {
    suspend fun load() = withContext(coroutineContextProvider.io) { ... }
}
```

### No Not-Null Assertions in Production

Never use `!!` or `requireNotNull` in production code — they crash on null. Use `?.let`,
`firstOrNull()`, `?: return`, or safe cast `as?`.

```kotlin
// ❌ WRONG
val item = vault.items.first()!!

// ✅ CORRECT
val item = vault.items.firstOrNull() ?: return Reducer.Result(state = getState())
```

### Error Handling — Prefer AppResult for Fallible Work

`AppResult<T>` (`Success` / `Failure`) lives in `core/mvi`. For operations that can fail
(decryption, file load, import/export parsing), return `AppResult` rather than throwing or
letting exceptions escape into reducers.

### Kotlin Idioms

- Use `lastIndex` instead of `size - 1`.
- Keep `enum class` / `data class` as pure data holders — move derived logic to extension functions in the same file.

### Functions

Define private helpers as members of the class that uses them, not as top-level functions —
unless they are genuinely standalone utilities with no owning class.

### Time

Use `kotlinx.datetime` / injected time sources rather than platform-specific clock calls, so
time can be controlled in tests. Use clear unit conversions rather than magic numbers
(`60 * 1000`).
