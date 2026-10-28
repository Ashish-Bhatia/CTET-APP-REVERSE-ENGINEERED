package org.ctetrebuild

import org.junit.Assert.*
import org.junit.Test

class MockSectionsTest {
    @Test fun paperIHasFiveThirtyQuestionSections() {
        val sections = MockSections(ExamBlueprintFactory.create(Paper.I))
        assertEquals(5, sections.ranges.size)
        assertEquals(listOf(0, 30, 60, 90, 120), sections.ranges.map { it.start })
        assertEquals(150, sections.ranges.last().endExclusive)
        assertEquals("p1-maths", sections.sectionAt(90).subject.id)
    }

    @Test fun paperIISpecializationHasSixtyQuestionFinalSection() {
        val sections = MockSections(
            ExamBlueprintFactory.create(Paper.II, PaperIISpecialization.SOCIAL_STUDIES)
        )
        assertEquals(4, sections.ranges.size)
        assertEquals(90, sections.firstQuestionOf("p2-social"))
        assertEquals(60, sections.sectionAt(149).count)
    }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsOutOfRangeQuestionIndex() {
        MockSections(ExamBlueprintFactory.create(Paper.I)).sectionAt(150)
    }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsUnknownSection() {
        MockSections(ExamBlueprintFactory.create(Paper.I)).firstQuestionOf("missing")
    }
}
