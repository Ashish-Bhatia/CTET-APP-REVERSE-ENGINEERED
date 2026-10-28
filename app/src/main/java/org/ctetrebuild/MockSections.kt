package org.ctetrebuild

/** Maps CTET blueprint sections to contiguous zero-based question ranges. */
class MockSections(val blueprint: ExamBlueprint) {
    data class Range(val subject: Subject, val start: Int, val endExclusive: Int) {
        val count: Int get() = endExclusive - start
        fun contains(index: Int): Boolean = index in start until endExclusive
    }

    val ranges: List<Range> = buildList {
        var offset = 0
        for (subject in blueprint.subjects) {
            add(Range(subject, offset, offset + subject.questions))
            offset += subject.questions
        }
    }

    init {
        require(ranges.lastOrNull()?.endExclusive == blueprint.totalQuestions)
    }

    fun sectionAt(index: Int): Range {
        require(index in 0 until blueprint.totalQuestions)
        return ranges.first { it.contains(index) }
    }

    fun firstQuestionOf(subjectId: String): Int =
        ranges.firstOrNull { it.subject.id == subjectId }?.start
            ?: throw IllegalArgumentException("Unknown section: $subjectId")
}
