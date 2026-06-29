---
paths:
  - "**/commonTest/**/*.kt"
  - "**/*Test.kt"
---

## Tests

Tests live in `commonMain`/`commonTest` and use `kotlin.test` (this is a Kotlin Multiplatform
project — keep tests tool-agnostic and runnable on all targets; avoid JVM-only frameworks in
`commonTest`). Coverage is currently sparse — new business logic (reducers, mappers, crypto,
sorting, import/export parsing) should come with tests.

Run tests:

```bash
./gradlew allTests                       # all targets
./gradlew :features:settings:allTests    # one module
```

### Structure

- **One test class per production class**, named `<ClassUnderTest>Test.kt`.
- Create the class under test and shared fixtures in a single setup block, not duplicated per test.
- Use descriptive backtick test names; no `// Given / When / Then` comments.
- Use imports, never fully-qualified names inside test bodies.

### Assert the Whole Result

Compare the whole returned object, not individual fields. Extract the expected value into a
`val expected` before asserting.

```kotlin
// ❌ WRONG
assertEquals(SortOrder.NAME_ASC, result.state.sortOrder)

// ✅ CORRECT
val expected = Reducer.Result(state = State.Success(passwords = sortedItems, sortOrder = SortOrder.NAME_ASC))
assertEquals(expected, result)
```

### Reducer Tests

Every reducer test should assert its `actionClass`, then exercise `reduce()` for each meaningful
branch of the action/state (success, empty, error, filter variants).

```kotlin
@Test
fun `action class`() {
    assertEquals(PasswordListScreenAction.InitScreen::class, reducer.actionClass)
}
```

Since reducers call repositories directly, substitute test doubles for the `data` repositories
(`VaultRuntimeRepository`, etc.) — a hand-written fake in `commonTest` works on all targets.

### Shared Test Data

Reuse fixtures across tests via a single `TestData` object per module rather than rebuilding
complex object graphs inline in each test. Keep single-use primitives inline.
