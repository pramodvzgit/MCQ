package com.mcq.exam.data.remote.dto

import com.google.gson.annotations.SerializedName

data class QuestionDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("question_text")
    val questionText: String,
    @SerializedName("explanation")
    val explanation: String?,
    @SerializedName("difficulty")
    val difficulty: String,
    @SerializedName("marks")
    val marks: Double,
    @SerializedName("negative_marks")
    val negativeMarks: Double,
    @SerializedName("options")
    val options: List<OptionDto>
)

data class OptionDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("option_text")
    val optionText: String,
    @SerializedName("option_order")
    val optionOrder: Int,
    @SerializedName("is_correct")
    val isCorrect: Boolean? = null
)

data class StartExamResponse(
    @SerializedName("attempt_id")
    val attemptId: String,
    @SerializedName("exam")
    val exam: ExamDto,
    @SerializedName("questions")
    val questions: List<QuestionDto>,
    @SerializedName("duration_minutes")
    val durationMinutes: Int
)

data class SubmitExamRequest(
    @SerializedName("answers")
    val answers: List<AnswerRequest>
)

data class AnswerRequest(
    @SerializedName("question_id")
    val questionId: String,
    @SerializedName("selected_option_id")
    val selectedOptionId: String
)

data class SubmitExamResponse(
    @SerializedName("attempt")
    val attempt: AttemptDto,
    @SerializedName("questions")
    val questions: List<QuestionDto>,
    @SerializedName("passed")
    val passed: Boolean
)

data class AttemptDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("exam")
    val exam: ExamDto,
    @SerializedName("started_at")
    val startedAt: String,
    @SerializedName("submitted_at")
    val submittedAt: String?,
    @SerializedName("score")
    val score: Double?,
    @SerializedName("percentage")
    val percentage: Double?,
    @SerializedName("correct_answers")
    val correctAnswers: Int,
    @SerializedName("incorrect_answers")
    val incorrectAnswers: Int,
    @SerializedName("unanswered_questions")
    val unansweredQuestions: Int,
    @SerializedName("status")
    val status: String
)

data class ReviewResponse(
    @SerializedName("attempt")
    val attempt: AttemptDto,
    @SerializedName("questions")
    val questions: List<QuestionWithAnswerDto>
)

data class QuestionWithAnswerDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("question_text")
    val questionText: String,
    @SerializedName("explanation")
    val explanation: String?,
    @SerializedName("difficulty")
    val difficulty: String,
    @SerializedName("marks")
    val marks: Double,
    @SerializedName("negative_marks")
    val negativeMarks: Double,
    @SerializedName("options")
    val options: List<OptionDto>,
    @SerializedName("user_answer")
    val userAnswer: UserAnswerDto?
)

data class UserAnswerDto(
    @SerializedName("option_id")
    val optionId: String?,
    @SerializedName("is_correct")
    val isCorrect: Boolean?,
    @SerializedName("marks_awarded")
    val marksAwarded: Double?
)
