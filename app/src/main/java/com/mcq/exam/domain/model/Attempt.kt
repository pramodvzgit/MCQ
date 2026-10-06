package com.mcq.exam.domain.model

import java.util.Date

data class Attempt(
    val id: String,
    val exam: Exam,
    val startedAt: Date,
    val submittedAt: Date?,
    val score: Double?,
    val percentage: Double?,
    val correctAnswers: Int,
    val incorrectAnswers: Int,
    val unansweredQuestions: Int,
    val status: AttemptStatus
)

enum class AttemptStatus {
    IN_PROGRESS,
    SUBMITTED,
    TIMED_OUT
}

data class ExamSession(
    val attemptId: String,
    val exam: Exam,
    val questions: List<Question>,
    val durationMinutes: Int,
    val startTime: Long
)
