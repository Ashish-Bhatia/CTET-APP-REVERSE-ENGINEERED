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
