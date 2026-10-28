# CTET Practice — Independent Android Rebuild

Independent, work-in-progress Android CTET preparation application informed by static analysis of a user-supplied reference APK.

## Current features (source implemented; not device-verified)
- Paper I and Paper II subject navigation
- Original demonstration practice questions
- Timed quiz sessions and answer review
- Basic latest-result storage per paper
- 150-question Paper I and Paper II exam blueprints
- Paper II Mathematics & Science **or** Social Studies specialization selection in the blueprint model
- Unit-test source for quiz engine, session timer, catalog, and exam blueprint

## Important limitations
This is **not** a complete replica or production-ready release. The Android build has not been compiled or emulator-tested. The app currently has no full question bank, in-progress session restoration, full-paper test UI, or backend. Paper II specialization selection is modeled but not yet wired to a full-paper UI.

## Build prerequisites
Android Studio with Android SDK API 36, Java 17, and compatible Gradle. The Gradle wrapper has not yet been added. After configuring the build environment, run `gradle testDebugUnitTest assembleDebug` and record results.

## Documentation
- [APK baseline](docs/APK_BASELINE.md)
- [Manifest audit](docs/MANIFEST_AUDIT.md)
- [Implementation status](docs/IMPLEMENTATION_STATUS.md)
- [Build validation](docs/BUILD_VALIDATION.md)

## Legal and privacy
No proprietary EduRev code, question bank, or private backend credentials are included. This project is an independent implementation and is not affiliated with EduRev.
