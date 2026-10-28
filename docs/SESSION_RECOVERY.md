# Session recovery checkpoint

Implemented source modules: `SessionPersistence.kt` and `QuestionNavigator.kt`.

- `SessionPersistence` serializes a subject ID, answer map, and absolute deadline to private Android SharedPreferences. It validates version and handles malformed JSON.
- `QuestionNavigator` provides bounded next/previous and indexed navigation.
- `QuestionNavigatorTest` contains three unit tests, not yet executed.

## Important limitations

These modules are **not yet integrated into MainActivity**. The existing activity continues to use a transient two-question quiz. Do not claim that resume or one-question-at-a-time navigation works in the UI.

## Next implementation steps

1. Wire session persistence to answer selection, app backgrounding, and explicit save/discard.
2. On launch, detect saved sessions and offer resume. Compute remaining time from deadline, not from a resettable countdown.
3. Add one-question-at-a-time navigation and answer palette.
4. Persist submissions and test history without duplicating results.
5. Build with Android SDK, execute unit tests, and verify process-death recovery on emulator.

No proprietary EduRev content should be committed.
