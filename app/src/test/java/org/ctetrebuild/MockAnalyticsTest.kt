package org.ctetrebuild

import org.junit.Assert.*
import org.junit.Test

class MockAnalyticsTest {
    private fun mock(): MockExam {
        val blueprint = ExamBlueprintFactory.create(Paper.I)
        val questions = blueprint.subjects.flatMap { subject ->
            (1..subject.questions).map { index ->
                Question("${subject.id}-${index}", "Fixture question", listOf("A", "B"), 0)
            }
        }
        return MockExam(blueprint, questions)
    }

    @Test fun emptyExamHasAllUnanswered() {
        val sections = MockAnalytics.sections(mock())
        assertEquals(5, sections.size)
        assertTrue(sections.all { it.total == 30 && it.unanswered == 30 && it.correct == 0 })
    }

    @Test fun scoreIsSeparatedBySection() {
        val exam = mock()
        exam.answer(0)
        exam.navigator.goTo(1)
        exam.answer(1)
        exam.navigator.goTo(30)
        exam.answer(0)
        val sections = MockAnalytics.sections(exam)
        assertEquals(1, sections[0].correct)
        assertEquals(1, sections[0].incorrect)
        assertEquals(28, sections[0].unanswered)
        assertEquals(1, sections[1].correct)
        assertEquals(29, sections[1].unanswered)
        assertEquals(2, sections.sumOf { it.correct })
        assertEquals(147, sections.sumOf { it.unanswered })
    }
}
