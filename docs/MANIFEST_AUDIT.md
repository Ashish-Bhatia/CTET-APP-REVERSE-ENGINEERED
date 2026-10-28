# Android manifest audit — 2026-10-28

## Verified source
User-provided CTET APK, SHA-256 `2c82aa916519476aef3dc0c70a509baafd9d33d59e05c8cfdd4e18d425034b4c`.

Parsed binary Android XML string pool and start-element attribute records locally. Counts refer to declarations, not independently tested features.

| Field | Verified value |
| --- | --- |
| Package | `com.edurev.ctet` |
| Version name | `6.1.6_ctet` |
| Version code | `616` |
| Compile SDK | `36` |
| Minimum SDK | `32` |
| Target SDK | `36` |
| Manifest start elements | `696` |
| Activity declarations | `138` |
| Service declarations | `24` |
| uses-permission declarations | `32` |
| DEX files | `4` |

The manifest declares `requiredSplitTypes=base__abi,base__density`; verify whether this APK alone is sufficient for installation and runtime.

### Permission inventory
- Network: `INTERNET`, `ACCESS_NETWORK_STATE`, `ACCESS_WIFI_STATE`.
- Media and device: `CAMERA`, `RECORD_AUDIO`, `VIBRATE`, `WAKE_LOCK`.
- Storage: `READ_EXTERNAL_STORAGE`, `WRITE_EXTERNAL_STORAGE`.
- Notifications and background: `POST_NOTIFICATIONS`, `FOREGROUND_SERVICE`, `FOREGROUND_SERVICE_MEDIA_PLAYBACK`, `FOREGROUND_SERVICE_DATA_SYNC`, `SCHEDULE_EXACT_ALARM`, `RECEIVE_BOOT_COMPLETED`.
- Identity and attribution: `USE_BIOMETRIC`, `USE_FINGERPRINT`, `AD_ID`, related Google services and attribution declarations.
- Other platform and integration declarations are present.

A permission declaration does not establish actual usage. Some manifest components belong to embedded SDKs.

### Evidence of embedded dependencies
The APK includes Firebase and Google Play Services property markers for analytics, authentication, messaging, identity, maps, wallet, and other modules. Presence is not proof that all modules are exercised.

### Next gates
1. Extract sanitized activity, receiver, service, provider, and launcher inventories from the manifest.
2. Classify application-owned components versus SDK components.
3. Build a verified screen/feature matrix from permitted runtime inspection and assets.
4. Start independently authored Android project with testable navigation and local mock-test domain model.
5. Validate with an Android SDK/emulator before claiming an installable release.

Do not publish extracted proprietary APK content, credentials, tokens, or private API endpoints. Keep implementation independent.
