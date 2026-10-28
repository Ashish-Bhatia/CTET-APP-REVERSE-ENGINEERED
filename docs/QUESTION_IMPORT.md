# Offline question import — schema v1

The app includes a strict `QuestionBankJson.parse(raw)` importer for a JSON document containing `schemaVersion: 1` and a `questions` array.

Each question must have `id`, `subjectId`, `prompt`, `options`, `correctIndex`, and `sourceLicense`. `explanation` is optional. Duplicate IDs, empty licenses, invalid options, and invalid answer indices are rejected by `QuestionBank`.

A two-item original-content fixture is at `app/src/main/assets/questions-demo.json`. This is not a complete CTET question bank and is not yet wired into the activity.

Import validation does **not** verify legal rights to third-party content. Only use independently authored or appropriately licensed material. Never distribute extracted proprietary EduRev questions.

## Pending

- Wire JSON loading into Android UI.
- Add asset-level and malformed-document tests.
- Obtain verified licensed/original full-paper content.
- Run Android build and device tests.

## Phase 12 implementation

The Android activity now reads `questions-demo.json` from bundled assets and selects matching questions for the chosen subject. If the asset fails to load or has no questions for that subject, it uses original in-code demonstration questions. This fallback is intended for early development only; full mock tests must fail closed when coverage is insufficient.

Five `QuestionBankJsonTest` JVM test cases were added. The app Gradle file declares `org.json:json` for local tests. **No tests or Android builds have been executed yet.**

Remaining: per-record provenance verification, richer content format, UI-level import failure reporting, full-paper content, build and device verification.
