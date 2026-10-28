# Question bank contract

The independent app now includes `QuestionRecord` and `QuestionBank`.

## Required record fields
- Stable, globally unique question ID
- CTET subject ID from `ExamCatalog`
- Non-empty prompt and at least two non-empty options
- Correct answer index in bounds
- Explanation (optional)
- Non-empty source-license/provenance label

## Full-paper assembly
`QuestionBank.forBlueprint` requires enough records for **every** selected subject. It fails rather than silently presenting an incomplete mock test. Paper I and either Paper II specialization each require 150 questions. Paper II specialization groups are alternatives.

## Current limitations
- No full licensed question corpus has been imported.
- No import parser or provenance-verification workflow exists yet.
- The app's UI still runs short original demonstration practices.
- Unit tests are committed but **not executed**; Android SDK and Gradle are unavailable in the current environment.
- License metadata is a validation field, **not proof of licensing**. Content rights must be checked before distribution.

## Next steps
1. Add a strict offline JSON import format and fixture validation.
2. Provide a sufficiently licensed/original question corpus.
3. Wire the full-paper blueprint and specialization selection into the UI.
4. Install SDK/Gradle and execute all unit tests plus emulator checks.
