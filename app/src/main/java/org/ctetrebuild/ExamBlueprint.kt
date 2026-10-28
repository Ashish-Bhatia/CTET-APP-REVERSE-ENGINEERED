package org.ctetrebuild

enum class PaperIISpecialization { MATHEMATICS_SCIENCE, SOCIAL_STUDIES }

data class ExamBlueprint(
    val paper: Paper,
    val subjects: List<Subject>,
    val durationMinutes: Int = 150
) {
    val totalQuestions: Int get() = subjects.sumOf { it.questions }
    init {
        require(durationMinutes > 0)
        require(totalQuestions == 150) { "CTET full paper must contain 150 questions" }
        require(subjects.map { it.id }.distinct().size == subjects.size)
    }
}

object ExamBlueprintFactory {
    fun create(
        paper: Paper,
        specialization: PaperIISpecialization? = null
    ): ExamBlueprint {
        if (paper == Paper.I) {
            require(specialization == null) { "Paper I does not use Paper II specialization" }
            return ExamBlueprint(paper, ExamCatalog.paperI.subjects)
        }
        requireNotNull(specialization) { "Choose a Paper II specialization" }
        val shared = ExamCatalog.paperII.subjects.take(3)
        val optional = when (specialization) {
            PaperIISpecialization.MATHEMATICS_SCIENCE -> ExamCatalog.paperII.subjects[3]
            PaperIISpecialization.SOCIAL_STUDIES -> ExamCatalog.paperII.subjects[4]
        }
        return ExamBlueprint(paper, shared + optional)
    }
}
