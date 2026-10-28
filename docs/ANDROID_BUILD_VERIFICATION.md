# Reproducible Android build and validation

## Verified repository configuration

- Android Gradle Plugin: 8.9.2
- Kotlin Android plugin: 2.1.20
- compileSdk / targetSdk: 36
- minSdk: 32
- JDK target: 17
- Project has **no Gradle wrapper** at this checkpoint.

## Prerequisites

Install JDK 17, Android SDK Platform 36, Android SDK Build Tools, and a Gradle distribution compatible with AGP 8.9.2 (Gradle 8.11.1). Accept Android SDK licenses and configure `ANDROID_HOME` / `JAVA_HOME`.

From the repository root:

```bash
java -version
gradle --version
gradle --no-daemon :app:testDebugUnitTest
gradle --no-daemon :app:assembleDebug
```

The APK, if compilation succeeds, should be under `app/build/outputs/apk/debug/`. This path is an expected Gradle output, **not** a currently verified artifact.

## Manual Android validation matrix

1. Launch the app and navigate both CTET papers.
2. Run the bundled short practice, answer/clear, submit, inspect results and history.
3. Save short practice, terminate/restart app, resume, verify deadline does not reset.
4. Verify mock readiness rejects the incomplete bundled question bank.
5. In a development-only licensed/original 150-question fixture, start Paper I and both Paper II specializations; verify section jumps, navigation, marks, answer clearing and countdown.
6. Save full mock, force-stop and reopen, verify answer/mark/position restoration and original deadline.
7. Verify expiry triggers submission and completed results appear in mock history with correct section scores.
8. Verify malformed saved state is handled safely, screen rotation, Android Back behavior, accessibility labels, and no unexpected crashes.

Do not claim a successful build or release until the above is executed and failures are resolved.

## Local verification — 28 October 2026

- Environment: OpenJDK 21.0.12.1, Kotlin CLI 1.9.0.
- `gradle` and `sdkmanager` not available.
- Reconstructed the repository's `SessionClock.kt` source in an isolated local directory and compiled it with Kotlin CLI 1.9.0.
- Executed four standalone checks: countdown rounding, exact expiry, normal remaining time and negative-epoch overflow saturation. **4/4 passed.**
- This was **not** the project's JUnit suite, Android compilation, or emulator validation. Kotlin CLI version differs from the project's Kotlin Android plugin.
- No custom GitHub Actions workflow is used or required.
