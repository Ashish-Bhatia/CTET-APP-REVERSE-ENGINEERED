#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
command -v kotlinc >/dev/null || { echo "Install Kotlin CLI (kotlinc)" >&2; exit 1; }
command -v java >/dev/null || { echo "Install Java" >&2; exit 1; }
workdir="$(mktemp -d)"
trap 'rm -rf "$workdir"' EXIT
src="app/src/main/java/org/ctetrebuild"
kotlinc \
  "$src/ExamCatalog.kt" \
  "$src/ExamBlueprint.kt" \
  "$src/QuizEngine.kt" \
  "$src/QuestionNavigator.kt" \
  "$src/MockSections.kt" \
  "$src/MockExam.kt" \
  "$src/MockAnalytics.kt" \
  tools/CoreSmoke.kt \
  -include-runtime -d "$workdir/core-smoke.jar"
java -jar "$workdir/core-smoke.jar"
echo "Scope: Android-independent Kotlin core only; not Gradle, JUnit or an APK build."
