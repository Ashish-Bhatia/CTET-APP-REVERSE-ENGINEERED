package org.ctetrebuild

import org.junit.Assert.*
import org.junit.Test

class ExamCatalogTest {
    @Test fun paperIHas150Questions() {
        assertEquals(150, ExamCatalog.paperI.totalQuestions)
        assertEquals(5, ExamCatalog.paperI.subjects.size)
    }

    @Test fun paperIIHasTwoAlternative60QuestionGroups() {
        assertEquals(150, ExamCatalog.paperII.totalQuestions)
        assertEquals(60, ExamCatalog.paperII.subjects[3].questions)
        assertEquals(60, ExamCatalog.paperII.subjects[4].questions)
    }

    @Test fun subjectsHaveUniqueIds() {
        val ids = ExamCatalog.tracks.flatMap { it.subjects }.map { it.id }
        assertEquals(ids.size, ids.distinct().size)
    }
}
