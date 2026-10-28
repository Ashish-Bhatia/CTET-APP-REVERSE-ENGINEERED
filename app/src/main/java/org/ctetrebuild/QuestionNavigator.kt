package org.ctetrebuild

class QuestionNavigator(questionIds: List<String>) {
    val questionIds = questionIds.toList()
    var index: Int = 0
        private set

    init {
        require(this.questionIds.isNotEmpty())
        require(this.questionIds.distinct().size == this.questionIds.size)
    }

    val currentId: String get() = questionIds[index]
    val hasNext: Boolean get() = index < questionIds.lastIndex
    val hasPrevious: Boolean get() = index > 0

    fun goTo(position: Int): String {
        require(position in questionIds.indices)
        index = position
        return currentId
    }

    fun next(): String = goTo((index + 1).coerceAtMost(questionIds.lastIndex))
    fun previous(): String = goTo((index - 1).coerceAtLeast(0))
}
