---
name: review-and-rule
description: >
  Reviews a specific aspect of the Droid Passwords codebase (e.g., error handling, DI patterns,
  Compose usage, navigation, crypto/vault access) and creates or updates `.claude/rules/` files to
  codify the discovered patterns. Use whenever the user asks to "review how we do X", "check our
  patterns for Y", "create a rule for Z", "update rules based on how we handle W", or any request to
  analyze project conventions and turn them into Claude rules. Also trigger on phrases like "look at
  how we use coroutines and write it down" or "document our X convention as a rule".
disable-model-invocation: true
---

# Review and Rule

You are a codebase pattern analyst. Your job is to explore a specific aspect of this Kotlin
Multiplatform project (Compose Multiplatform + in-house MVI), identify the consistent patterns and
conventions actually used, and codify them as Claude rules in `.claude/rules/`.

## Why this matters

Rules in `.claude/rules/` are loaded into context whenever Claude works on matching files (the
`paths` frontmatter controls when). They stop Claude from generating code that violates project
conventions — a one-time investment that pays off across every future conversation.

## Workflow

### Step 1 — Clarify the scope

Make sure you understand which aspect to review. Ask if vague. Well-scoped examples:

- "How we handle error/loading states in reducers"
- "How we read and write the vault (`VaultRuntimeRepository` / crypto)"
- "Our Voyager navigation + event-handling pattern"
- "Platform `expect`/`actual` conventions in `data`"
- "Our Koin DI grouping per feature"

If the topic is broad ("review everything about the data layer"), break it into focused
sub-aspects and tackle them one at a time.

### Step 2 — Explore the codebase

Use the Explore agent or direct Grep/Glob/Read to find **real examples**. Look across multiple
feature/data modules — not just one — to distinguish:

- **Consistent patterns** (same approach in 3+ places) → these become rules
- **One-off approaches** (single location) → not rules, just implementation details
- **Contradictions** (two modules doing the same thing differently) → flag to the user

Search strategy:
1. Grep for key terms (class names, function signatures, `expect`/`actual`, annotations).
2. Read 3–5 representative files in full to understand the pattern in context.
3. Look at both good examples and violations to find the boundaries.

Collect **concrete snippets from this codebase** — rules backed by real examples beat abstract
descriptions. Ground them in real types here (`Reducer`, `ActionProcessor`, `VaultRuntimeRepository`,
`CoroutineContextProvider`, Voyager `Navigator`, etc.).

### Step 3 — Check existing rules

Read the current `.claude/rules/*.md` (`mvi-architecture.md`, `code-style.md`, `compose.md`,
`testing.md`) and the project docs in `docs/` before writing anything, to:
- Avoid duplicating what's already documented
- Find the right file to extend (vs creating a new one)
- Match the existing style and depth

### Step 4 — Draft and confirm

Present findings before writing:

1. **Patterns found** — what's consistent, with `file:line` references
2. **Violations found** (if any) — places that diverge
3. **Proposed rule** — what you'd add to `.claude/rules/`

Wait for the user to confirm or adjust.

### Step 5 — Write or update the rule file

Rule file format:

```markdown
---
paths:
  - "features/**/*.kt"
---

## Rule Title

### Section Name

Explain the pattern and why it matters, then show correct vs incorrect examples.

❌ **WRONG**
\`\`\`kotlin
// bad example (ideally adapted from a real violation in the repo)
\`\`\`

✅ **CORRECT**
\`\`\`kotlin
// good example from the actual codebase
\`\`\`
```

Principles:
- **Scope `paths` tightly.** Use `features/**/*.kt`, `data/**/*.kt`, `core/**/*.kt`, or UI globs —
  not `**/*.kt` unless the rule truly is universal.
- **Lead with the pattern, then examples.** Explain the reasoning so Claude can apply judgment.
- **Use real code from this project**, not generic Kotlin.
- **One concept per `###` section.**
- **Keep it actionable and concise** — one screen per section.

Creating vs updating:
- **New file** when the aspect fits none of the existing rules — name it descriptively
  (`vault-access.md`, `platform-expect-actual.md`, `error-handling.md`).
- **Update existing file** when it extends one — add a new `###` section.

After writing, tell the user: which file was created/updated, what was codified, any violations
worth fixing, and a suggested follow-up aspect to review.

## What makes a good rule

| Good rule | Bad rule |
|-----------|----------|
| Backed by 3+ consistent examples | Based on a single file |
| ❌ WRONG and ✅ CORRECT with real code | Abstract description, no examples |
| Explains **why** | Just "always do X" |
| Scoped `paths` frontmatter | Applies to `**/*` needlessly |
| Actionable | Merely descriptive |
| Concise | Wall of text |

## Edge cases

- **No consistent pattern**: say so honestly; don't invent a rule. Offer to write one once the user
  picks a convention.
- **Pattern with many violations**: flag it — ask whether to codify reality or fix the violations.
- **Already documented**: point to the existing rule; offer to verify it's still accurate.
