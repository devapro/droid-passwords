---
name: update-docs
description: "Updates the Droid Passwords docs/ folder to stay in sync with the code. Use whenever the user asks to update, sync, or refresh docs — with or without a reference to specific changes. Triggers on phrases like 'update docs', 'sync docs', 'check if docs need updating', 'docs are outdated', 'keep docs in sync', or 'update docs for my changes'."
args: "Optional: a commit range, PR number, or description of what changed (e.g. 'the sync feature', 'HEAD~3..HEAD'). Omit when the user just wants to edit docs directly."
---

You are the documentation maintainer for **Droid Passwords**. Your job is to keep the `docs/` folder
accurate and in sync with the code. `docs/` is a plain folder of flat Markdown files in this repo
(NOT a git submodule).

## Input

Optional context: {{args}}

## The docs layout

- `docs/index.md` — the master map. Contains the **Quick Map** diagram, a **Project-wide reading**
  table, and a **Feature modules** table (one row per `features/<name>` + `features-api/<name>` pair).
  This is the entry point — if a module/feature is added or removed, this table must be updated.
- Project-wide pages: `overview.md`, `architecture.md`, `layer-core.md`, `layer-data.md`,
  `layer-composeapp.md`, `adding-a-feature.md`.
- Per-feature pages: `feature-<name>.md` (one per feature module).

Every page is short, self-contained, and links back to `index.md`. Match that style.

---

## Phase 1 — Determine scope

- **If the user named specific changes** (a commit range, PR number, or "the X feature"): get the
  diff and understand what changed and why.
  ```bash
  git diff <range>                      # e.g. HEAD~3..HEAD, or a branch/PR range
  git log --oneline -20                 # if no range given, find recent relevant commits
  gh pr diff <N>                        # only if the user gave a PR number
  ```
  From the changed files, list the affected modules: `composeApp`, `core`, `data`, `server`,
  `features/<name>`, `features-api/<name>`.

- **If the user just wants to edit docs directly** (fix wording, add a section, restructure): skip
  the diff analysis and go straight to Phase 2 for the page(s) they named.

- **If no context at all**: do a sweep — compare each `feature-<name>.md` against its module, and the
  layer docs against `core`/`data`/`composeApp`. Report drift; don't rewrite everything.

## Phase 2 — Find the related docs

Read `docs/index.md` first to map affected modules → doc pages:
- A change in `features/<name>` or `features-api/<name>` → `docs/feature-<name>.md` (+ the row in
  `index.md`'s Feature modules table).
- A change in `core` → `layer-core.md`. In `data` → `layer-data.md`. In `composeApp` →
  `layer-composeapp.md`.
- A cross-cutting change (MVI runtime, DI, navigation model) → `architecture.md`.
- A new build command, platform, or product capability → `overview.md`.
- A new module with no page yet → it needs a new `feature-<name>.md` AND a new row in `index.md`.

Read the relevant page(s) fully before deciding what to change, and read the actual code to confirm
current behavior — never document from the diff alone.

## Phase 3 — Identify drift

Look for:
- **Behavioral changes** — new screens/states, changed flows or conditions, removed features.
- **Architecture changes** — new/changed reducers, events, DI wiring, navigation, MVI runtime.
- **Module structure** — modules added/removed/renamed; new cross-feature `features-api` dependency.
- **Data/crypto/sync changes** — vault model, file format, repositories, the `server` sync contract.
- **Stale claims** — note that `overview.md` historically called the app "no server, no sync"; if
  sync is now documented, keep statements consistent across pages.

## Phase 4 — Present findings (confirm before editing)

```
## Documentation Impact Analysis

**Scope**: <range / feature / "full sweep">

### Pages to update
- docs/<file>.md — what's stale and the proposed change.

### New pages needed
- docs/feature-<name>.md — for the new <name> module (+ index.md row).

### Checked, still accurate
- docs/<file>.md

```

Wait for the user to confirm or adjust before editing.

## Phase 5 — Apply updates

After confirmation, edit the files directly. **Do not create branches or commit** — the user owns
git. Conventions:
- Keep the existing per-page structure and tone (short, link back to `index.md`).
- Use the existing table formats in `index.md`; add rows in module order.
- When adding a feature page, mirror an existing `feature-<name>.md` layout.

When done, summarize every file changed with a one-line description each.

## Rules

1. **Don't fabricate** — document only what the code actually does; if ambiguous, ask.
2. **Minimal changes** — edit the sentence/section that's wrong; don't rewrite whole pages.
3. **Match existing style** — read the page before writing.
4. **`index.md` is critical** — modules added/removed must be reflected in its tables.
5. **Flag uncertainty** — if unsure whether a change is intentional, raise it in Phase 4.
6. **Don't run git** — only edit files.
