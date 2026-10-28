package org.ctetrebuild

/** Standalone JVM smoke checks; run with tools/verify-core.sh, no Android SDK needed. */
fun main() {
    val configurations = listOf(
        ExamBlueprintFactory.create(Paper.I),
        ExamBlueprintFactory.create(Paper.II, PaperIISpecialization.MATHEMATICS_SCIENCE),
        ExamBlueprintFactory.create(Paper.II, PaperIISpecialization.SOCIAL_STUDIES)
    )
    var checks = 0
    configurations.forEach { blueprint ->
        val sections = MockSections(blueprint)
        check(blueprint.totalQuestions == 150)
        check(sections.ranges.sumOf { it.count } == 150)
        val questions = blueprint.subjects.flatMap { subject ->
            (1..subject.questions).map { n ->
                Question("${subject.id}-${n}", "Independent test fixture", listOf("A", "B"), 0)
            }
        }
        val mock = MockExam(blueprint, questions)
        check(mock.unansweredCount == 150)
        mock.answer(0)
        mock.toggleReviewMark()
        mock.navigator.goTo(149)
        mock.answer(1)
        val restored = MockExam(blueprint, questions)
        restored.restore(mock.snapshotAnswers(), mock.snapshotMarked(), mock.navigator.index)
        check(restored.attemptedCount == 2 && restored.markedCount == 1)
        check(restored.navigator.index == 149)
        val scores = MockAnalytics.sections(restored)
        check(scores.sumOf { it.correct } == 1)
        check(scores.sumOf { it.incorrect } == 1)
        check(scores.sumOf { it.unanswered } == 148)
        try {
            MockExam(blueprint, questions).restore(
                linkedMapOf(questions[0].id to 0, questions[1].id to 99), emptySet(), 10
            )
            error("Invalid snapshot accepted")
        } catch (_: IllegalArgumentException) {
            // Expected: invalid option rejected before any answer is applied.
        }
        checks += 10
    }
    println("Core Kotlin smoke checks passed: $checks (three CTET configurations)")
}
