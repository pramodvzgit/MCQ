package com.mcq.exam.data.repository

import com.mcq.exam.data.remote.api.ExamApi
import com.mcq.exam.data.remote.dto.AnswerRequest
import com.mcq.exam.data.remote.dto.SubmitExamRequest
import com.mcq.exam.data.remote.mapper.DtoMapper
import com.mcq.exam.domain.model.ExamSession
import com.mcq.exam.domain.model.QuestionWithAnswer
import com.mcq.exam.domain.repository.IQuestionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionRepository @Inject constructor(
    private val examApi: ExamApi
) : IQuestionRepository {

    override suspend fun startExam(examId: String): Result<ExamSession> {
        return try {
            val response = examApi.startExam(examId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(DtoMapper.toDomain(response.body()!!))
            } else {
                Result.failure(Exception("Failed to start exam: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun submitExam(
        attemptId: String,
        answers: Map<String, String>
    ): Result<IQuestionRepository.SubmitResult> {
        return try {
            val answerRequests = answers.map { (questionId, optionId) ->
                AnswerRequest(questionId, optionId)
            }
            val request = SubmitExamRequest(answerRequests)
            val response = examApi.submitExam(attemptId, request)

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                Result.success(
                    IQuestionRepository.SubmitResult(
                        attemptId = body.attempt.id,
                        score = body.attempt.score ?: 0.0,
                        percentage = body.attempt.percentage ?: 0.0,
                        correctAnswers = body.attempt.correctAnswers,
                        incorrectAnswers = body.attempt.incorrectAnswers,
                        unansweredQuestions = body.attempt.unansweredQuestions,
                        passed = body.passed,
                        questions = body.questions.map { DtoMapper.toDomain(it) }
                    )
                )
            } else {
                Result.failure(Exception("Failed to submit exam: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAttemptReview(attemptId: String): Result<List<QuestionWithAnswer>> {
        return try {
            val response = examApi.getAttemptReview(attemptId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.questions.map { DtoMapper.toDomain(it) })
            } else {
                Result.failure(Exception("Failed to fetch review: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
