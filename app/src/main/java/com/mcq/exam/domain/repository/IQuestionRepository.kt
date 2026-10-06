package com.mcq.exam.domain.repository

import com.mcq.exam.domain.model.ExamSession
import com.mcq.exam.domain.model.Question
import com.mcq.exam.domain.model.QuestionWithAnswer

interface IQuestionRepository {
    suspend fun startExam(examId: String): Result<ExamSession>
    suspend fun submitExam(attemptId: String, answers: Map<String, String>): Result<SubmitResult>
    suspend fun getAttemptReview(attemptId: String): Result<List<QuestionWithAnswer>>
}

data class SubmitResult(
    val attemptId: String,
    val score: Double,
    val percentage: Double,
    val correctAnswers: Int,
    val incorrectAnswers: Int,
    val unansweredQuestions: Int,
    val passed: Boolean,
    val questions: List<QuestionWithAnswer>
)
