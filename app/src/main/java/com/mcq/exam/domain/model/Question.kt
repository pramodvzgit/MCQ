package com.mcq.exam.domain.model

data class Question(
    val id: String,
    val questionText: String,
    val explanation: String?,
    val difficulty: String,
    val marks: Double,
    val negativeMarks: Double,
    val options: List<Option>
)

data class Option(
    val id: String,
    val optionText: String,
    val optionOrder: Int,
    val isCorrect: Boolean? = null // Only available after submission
)

data class QuestionWithAnswer(
    val question: Question,
    val userAnswer: UserAnswer?
)

data class UserAnswer(
    val optionId: String?,
    val isCorrect: Boolean?,
    val marksAwarded: Double?
)
