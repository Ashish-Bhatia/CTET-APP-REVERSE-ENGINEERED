# APK baseline (2026-10-28)

Source: user-supplied `CTET App.apk`, reference package `com.edurev.ctet`.

- APK size: approximately 42 MiB.
- SHA-256: `2c82aa916519476aef3dc0c70a509baafd9d33d59e05c8cfdd4e18d425034b4c`
- ZIP entries: 5,971.
- DEX files: 4 (`classes.dex` through `classes4.dex`).
- Resource entries under `res/`: 5,037.
- Asset entries under `assets/`: 659.
- Confirmed embedded assets include HTML/CSS, `edurePlayer.js`, `edurevplyr.html`, font files, and SVGs.
- Manifest is present in binary Android XML; permissions, activities, and SDK versions have **not yet been decoded**.
- No claim is made that APK runtime behavior, authentication, APIs, or all screens have been validated.

## Next implementation gates

1. Decode manifest and resource table with Android tooling.
2. Generate activity, navigation, permissions, and network endpoint inventories; redact secrets and tokens.
3. Build a feature parity matrix with evidence for each screen and behavior.
4. Implement an independently authored Android application, using original or licensed content.
5. Run emulator/device tests, document gaps, and commit verified increments.

Do not commit the proprietary APK, extracted proprietary assets, private endpoints, or credentials to this public repository. Do not represent inferred features as verified. This document records only directly inspected archive metadata.
