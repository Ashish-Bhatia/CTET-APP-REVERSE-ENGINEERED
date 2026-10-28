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
