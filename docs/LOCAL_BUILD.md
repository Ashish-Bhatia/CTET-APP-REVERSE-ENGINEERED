# Local Android build verification

This repository does not contain a verified Gradle wrapper. Do not download or execute an unverified wrapper JAR.

## Prerequisites
- JDK 17
- Android SDK Platform 36 and the corresponding build tools
- A Gradle distribution compatible with Android Gradle Plugin 8.9.2
- Network access to Google Maven, Maven Central and Gradle Plugin Portal

## Procedure
1. Set `ANDROID_HOME` to the installed Android SDK directory.
2. Confirm `java -version` reports JDK 17.
3. Confirm `gradle --version` reports a compatible Gradle version.
4. From the repository root, run `gradle --no-daemon :app:testDebugUnitTest :app:assembleDebug`.
5. Inspect test reports under `app/build/reports/tests/testDebugUnitTest/`.
6. Inspect the debug APK under `app/build/outputs/apk/debug/`.
7. Install the APK on an emulator or device and verify subject navigation, imported JSON questions, persistence, expired sessions and history.
8. Record exact Gradle/SDK versions, test counts, APK hash and any runtime failures.

## Status
The build procedure is documented, **not executed**. The current execution environment has Java and Kotlin CLI but no Gradle or Android SDK. No APK or passing test report is claimed.

No custom GitHub Actions workflows are used.
