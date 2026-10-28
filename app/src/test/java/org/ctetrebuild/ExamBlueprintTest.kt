package org.ctetrebuild

import org.junit.Assert.*
import org.junit.Test

class ExamBlueprintTest {
    @Test fun paperIHasFiveSubjectsAnd150Questions() {
        val blueprint = ExamBlueprintFactory.create(Paper.I)
        assertEquals(5, blueprint.subjects.size)
        assertEquals(150, blueprint.totalQuestions)
        assertEquals(150, blueprint.durationMinutes)
    }

    @Test fun paperIIMathsScienceHas150Questions() {
        val blueprint = ExamBlueprintFactory.create(Paper.II, PaperIISpecialization.MATHEMATICS_SCIENCE)
        assertEquals(4, blueprint.subjects.size)
        assertEquals(150, blueprint.totalQuestions)
        assertEquals("p2-maths-science", blueprint.subjects.last().id)
    }

    @Test fun paperIISocialStudiesHas150Questions() {
        val blueprint = ExamBlueprintFactory.create(Paper.II, PaperIISpecialization.SOCIAL_STUDIES)
        assertEquals(150, blueprint.totalQuestions)
        assertEquals("p2-social", blueprint.subjects.last().id)
    }

    @Test(expected = IllegalArgumentException::class)
    fun paperIIRequiresSpecialization() { ExamBlueprintFactory.create(Paper.II) }

    @Test(expected = IllegalArgumentException::class)
    fun paperIRejectsSpecialization() {
        ExamBlueprintFactory.create(Paper.I, PaperIISpecialization.SOCIAL_STUDIES)
    }
}
