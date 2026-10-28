# Full-paper mock examination engine

`MockExam` is a Kotlin-only domain component for the 150-question CTET blueprint.

## Implemented
- Requires an exactly 150-question blueprint-matched list
- One-question navigation through `QuestionNavigator`
- Answer and clear-answer actions
- Independent mark-for-review state
- Attempted and unanswered counts
- Submission lock against subsequent answer changes

## Important boundaries
- Use `QuestionBank.forBlueprint(blueprint)` to verify subject coverage **before** creating `MockExam`. A list of 150 arbitrary questions is not sufficient evidence of correct subject allocation.
- The 150-minute deadline, UI rendering, persisted session state and section navigation are **not yet wired** to `MockExam`.
- A complete original or properly licensed question corpus is **not yet available**.
- Unit tests are committed but **not executed**. No Android build or APK is verified.

## Next
1. Execute Kotlin/JUnit tests in a configured build environment.
2. Implement section-aware question navigation and an Android mock UI gated by `QuestionBank.forBlueprint`.
3. Add robust timed persistence/recovery and result analytics.
4. Perform emulator tests before distribution.

## Phase 18 UI integration

- The readiness screen now offers **Start full-paper mock** only after `QuestionBank.forBlueprint` validates 150 questions with correct subject allocation.
- `MainActivity` creates `MockExam`, displays one question at a time, offers section jumps, Previous/Next, answer clearing and review marking, and displays a 150-minute countdown and result summary.
- `MockSections` maps Paper I (5 × 30) and Paper II (3 × 30 + 1 × 60) to zero-based ranges. Four unit tests cover section boundaries.
- **Important limitation:** mock answers are currently in-memory only. Leaving the screen or process death loses mock progress. No background recovery is implemented. Do not present this as production ready.
- The bundled dataset does not meet full-paper requirements. No mock can start using the current fixture.
- No Android build, JUnit run or emulator validation has been completed.

## Phase 19 — local persistence and recovery

- `MockPersistence` stores a versioned JSON snapshot in app-private SharedPreferences, using synchronous commit.
- Snapshot includes paper, Paper II specialization, original absolute deadline, ordered question IDs, answer indices, marked question IDs and current index.
- `MockExam.restore` validates IDs/indices and restores answers, review marks and navigation position.
- `MainActivity` saves after mock interactions and exposes Resume/Discard on Home. Resume checks the current bundled question bank has the same ordered IDs and valid answer indices. Expired saved mocks submit immediately on opening.
- Completion clears the saved mock; a new mock clears any previous mock snapshot.
- Remaining limitations: no mock-history record, no answer-by-answer result review, no background completion while app is closed, and no Android build/device validation. SharedPreferences snapshots are local and may be lost when app data is cleared. The bundled question bank remains incomplete.

## Phase 20 — section analytics and answer review

- Added pure Kotlin `MockAnalytics.sections(exam)` to calculate per-subject correct, incorrect, attempted and unanswered counts.
- Results screen now displays per-section scores and offers a complete answer review (selected and correct options).
- Two JUnit tests cover empty and partially answered Paper I section scores.
- Answer review is available only immediately after submission; completed mock results are not persisted as a separate history record.
- The bundled question bank is still incomplete, and Android build/unit tests/emulator validation remain unexecuted.

## Phase 21 — completed mock history

- Added `MockHistory` with app-private SharedPreferences storage for up to 50 recent completed mock summaries.
- Each summary stores paper, specialization, completion timestamp, overall correct/attempted counts and per-section totals/correct/attempted.
- Submission records the summary before clearing the active mock snapshot. The Home screen offers Completed mock history and a clear-history action.
- Completed mock history stores **scores only**, not the full question/answer review. It does not replace the in-progress mock snapshot.
- The history storage uses synchronous commit; failures are not yet surfaced through a user-friendly recovery screen.
- Android build, JUnit execution and emulator validation are still pending; do not treat this as release-ready.
