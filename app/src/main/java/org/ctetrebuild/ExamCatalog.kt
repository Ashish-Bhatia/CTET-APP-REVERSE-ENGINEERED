package org.ctetrebuild

enum class Paper { I, II }

data class Subject(val id: String, val title: String, val questions: Int)

data class ExamTrack(val paper: Paper, val title: String, val subjects: List<Subject>) {
    val totalQuestions: Int get() = if (paper == Paper.II) 150 else subjects.sumOf { it.questions }
}

object ExamCatalog {
    val paperI = ExamTrack(Paper.I, "Paper I • Classes I–V", listOf(
        Subject("p1-cdp", "Child Development and Pedagogy", 30),
        Subject("p1-l1", "Language I", 30),
        Subject("p1-l2", "Language II", 30),
        Subject("p1-maths", "Mathematics", 30),
        Subject("p1-evs", "Environmental Studies", 30)
    ))

    val paperII = ExamTrack(Paper.II, "Paper II • Classes VI–VIII", listOf(
        Subject("p2-cdp", "Child Development and Pedagogy", 30),
        Subject("p2-l1", "Language I", 30),
        Subject("p2-l2", "Language II", 30),
        Subject("p2-maths-science", "Mathematics and Science", 60),
        Subject("p2-social", "Social Studies / Social Science", 60)
    ))

    val tracks = listOf(paperI, paperII)

    fun track(paper: Paper): ExamTrack = when (paper) {
        Paper.I -> paperI
        Paper.II -> paperII
    }
}
