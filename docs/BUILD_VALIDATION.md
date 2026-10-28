# Build and validation checkpoint — 2026-10-28

## Environment findings

- Java runtime available.
- Kotlin command-line compiler available.
- Gradle executable not installed in the local execution environment.
- Android SDK not configured.
- No emulator or physical-device runtime verification performed.
- Gradle wrapper scripts/JAR are not present in the repository.

**Build status: NOT VERIFIED.** No APK release has been produced. Do not label this project production-ready.

## Functional status

- Paper and subject navigation: implemented in source, not runtime-tested.
- Timed two-question practice: implemented in source, not runtime-tested.
- Answer review: implemented in source, not runtime-tested.
- Local latest-result persistence: implemented in source, not runtime-tested.
- Paper II specialization blueprint: implemented with separate 150-question plans; full-length content is not yet available.
- Source tests: authored but not yet executed.

## Required next gates

1. Install Android SDK API 36 and a compatible Gradle distribution, or run in Android Studio.
2. Add a verified Gradle wrapper, then run `./gradlew testDebugUnitTest assembleDebug`.
3. Fix build and test failures, and commit the exact outputs of the checks.
4. Add in-progress session persistence and restore across process recreation.
5. Add an original/licensed question corpus and ensure the number of questions matches the blueprint.
6. Validate on an Android emulator or device.

## Copyright and security

Do not commit the reference APK, extracted proprietary code, keys, private endpoints, or question bank to this public repository.

## Phase 9 checkpoint (2026-10-28)

- UI question-navigation visibility now applies to both prompts and answer groups.
- Submission guard prevents repeated history writes within the same activity session.
- Added private, bounded history of the latest 50 completed practice attempts with a clear action.
- Resume countdown initializes from the saved absolute deadline.
- **No Android build, JUnit run, emulator test, or process-death test has been completed.**
- Known limitation: full-paper delivery remains a blueprint only; the current practice screen uses two original sample questions per subject. History is local-only and has no migration or cross-device sync.

## Phase 11 validation attempt

- Added `QuestionBankJson.kt` and a two-item original JSON fixture.
- Attempted shallow Git clone for standalone Kotlin compilation in local container.
- Result: **blocked** (`Could not resolve host: github.com`); compilation was not attempted after the clone failure.
- No Android SDK/Gradle build, unit-test run, or emulator verification completed.

## Phase 13 checkpoint — 2026-10-28

- Verified GitHub `main` checkpoint before changes: `a28fa39d492225170645aa4d1303c240603d89b5`.
- Added `SessionClock` to compute remaining seconds from an absolute saved deadline, with four JVM test cases.
- Wired `MainActivity` to use `SessionClock` for initial remaining time, expiry detection and countdown updates.
- Environment checked: Java and Kotlin CLI available; Gradle and Android SDK not configured. No `assembleDebug` or `testDebugUnitTest` was run; no APK produced.
- Remaining blocker: establish a supported Android SDK/Gradle toolchain, execute compilation and tests, then perform emulator/device recovery tests.

## Phase 14 — 2026-10-28

- Local tool check: `java` and `kotlinc` available; `gradle` and Android SDK absent.
- Compiled a standalone local copy of the updated `SessionClock` source with `kotlinc` and ran a small JVM smoke harness.
- **Four smoke assertions passed:** initial countdown, final second, exact expiry, and extreme timestamp subtraction saturation.
- Scope limitation: the local harness was assembled separately, not built from a checked-out Git repository; it is not a complete repository test run.
- Added `docs/LOCAL_BUILD.md` with JDK 17 / SDK 36 / Gradle prerequisites and exact Gradle commands.
- Android unit tests, `assembleDebug`, emulator verification and installable APK remain **unverified**.

## Phase 15 — deadline enforcement and environment audit

- Verified `main` at `232055dd152630fe1f7aea811c297a161fa39e27` before changes.
- Practice answer, clear, previous/next, and save-return actions now check the absolute deadline before mutating session state; expired actions submit the test.
- Added a negative-epoch overflow regression test to `SessionClockTest` (written, not run).
- Local environment check in this turn: `java -version` reports OpenJDK **21.0.12**, not JDK 17; `kotlinc` exists; no `gradle` executable or Android SDK found in standard locations. An attempted `git ls-remote` failed because `github.com` could not be resolved.
- **No Android build, full test suite, emulator test or APK artifact verified.** A JDK 17 toolchain remains recommended for the specified Android Gradle Plugin.

## Phase 16 — mock readiness

- Added a user-facing mock-test readiness screen for Paper I and both Paper II specializations.
- Readiness shows per-subject counts and refuses to start incomplete mock tests. The full-paper examination UI is not implemented.
- Question bank now rejects unrecognized CTET subject IDs; one additional unit test was committed.
- Rechecked the local environment: `git ls-remote` failed with DNS resolution for github.com; no `gradle` or `sdkmanager` executable found. GitHub edits were made via the connected repository API.
- **No Android build, JUnit execution, emulator run, or APK verified in this phase.**

## Phase 17 — mock exam domain layer

- Verified previous `main` at `56bf571c767655f3075490408fa7dd106f4e9527`.
- Added `MockExam` with navigation, marking for review, answer clearing, attempted/unanswered counters and submission lock.
- Added five JVM unit tests and `docs/MOCK_EXAM.md`.
- Checked local toolchain: `java` and `kotlinc` available, Gradle and Android SDK absent. No build or test run performed.
- Full mock UI, content corpus, persistence and 150-minute timer remain pending.

## Phase 22 — recovery and history integrity

- Verified previous `main` HEAD: `418c48d536a7ec5ae480ab93f679eb6e8531e563`.
- Hardened `MockHistory.add` to reject wrong paper/section IDs, invalid section totals, impossible attempted/correct counts and nonpositive completion timestamps.
- Added three `MockExamTest` cases for snapshot round-trip and invalid saved answer/mark state.
- Checked local runtime: Kotlin compiler is installed, but neither `gradle` nor `sdkmanager` is on PATH. The Android build and all JUnit tests remain **unexecuted**.
- Do not publish an APK or describe the app as build-verified until an Android SDK/Gradle build and emulator smoke tests succeed.

## Phase 24 — atomic mock recovery

- Verified `main` before edits at `f7ea033269cc5ecd7c5df514c9cab7b48c7a0e4f`.
- `MockExam.restore` now validates every saved answer's option index before applying any answers, review marks or navigation state.
- Added a regression test confirming a snapshot containing a valid answer followed by an invalid answer does not partially restore state.
- Environment still has Kotlin CLI 1.9.0 but lacks Gradle and Android SDK command-line tools. **No Android compilation, JUnit execution or emulator testing occurred in Phase 24.**
- Release remains blocked until `:app:testDebugUnitTest` and `:app:assembleDebug` run successfully in an Android SDK environment.
