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
