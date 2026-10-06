package com.mcq.exam.data.remote.mapper

import com.mcq.exam.data.remote.dto.*
import com.mcq.exam.domain.model.*

object DtoMapper {
    fun toDomain(examDto: ExamDto): Exam {
        return Exam(
            id = examDto.id,
            title = examDto.title,
            description = examDto.description,
            subject = Subject(
                id = examDto.subject.id,
                name = examDto.subject.name
            ),
            durationMinutes = examDto.durationMinutes,
            totalQuestions = examDto.totalQuestions,
            passingPercentage = examDto.passingPercentage
        )
    }

    fun toDomain(questionDto: QuestionDto): Question {
        return Question(
            id = questionDto.id,
            questionText = questionDto.questionText,
            explanation = questionDto.explanation,
            difficulty = questionDto.difficulty,
            marks = questionDto.marks,
            negativeMarks = questionDto.negativeMarks,
            options = questionDto.options.map { toDomain(it) }
        )
    }

    fun toDomain(optionDto: OptionDto): Option {
        return Option(
            id = optionDto.id,
            optionText = optionDto.optionText,
            optionOrder = optionDto.optionOrder,
            isCorrect = optionDto.isCorrect
        )
    }

    fun toDomain(attemptDto: AttemptDto): Attempt {
        return Attempt(
            id = attemptDto.id,
            exam = toDomain(attemptDto.exam),
            startedAt = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(attemptDto.startedAt) ?: java.util.Date(),
            submittedAt = attemptDto.submittedAt?.let {
                java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(it)
            },
            score = attemptDto.score,
            percentage = attemptDto.percentage,
            correctAnswers = attemptDto.correctAnswers,
            incorrectAnswers = attemptDto.incorrectAnswers,
            unansweredQuestions = attemptDto.unansweredQuestions,
            status = when (attemptDto.status) {
                "in_progress" -> AttemptStatus.IN_PROGRESS
                "submitted" -> AttemptStatus.SUBMITTED
                "timed_out" -> AttemptStatus.TIMED_OUT
                else -> AttemptStatus.IN_PROGRESS
            }
        )
    }

    fun toDomain(userDto: UserDto): User {
        return User(
            id = userDto.id,
            name = userDto.name,
            email = userDto.email
        )
    }

    fun toDomain(questionWithAnswerDto: QuestionWithAnswerDto): QuestionWithAnswer {
        return QuestionWithAnswer(
            question = toDomain(questionWithAnswerDto as QuestionDto),
            userAnswer = questionWithAnswerDto.userAnswer?.let {
                UserAnswer(
                    optionId = it.optionId,
                    isCorrect = it.isCorrect,
                    marksAwarded = it.marksAwarded
                )
            }
        )
    }

    fun toDomain(startExamResponse: StartExamResponse): ExamSession {
        return ExamSession(
            attemptId = startExamResponse.attemptId,
            exam = toDomain(startExamResponse.exam),
            questions = startExamResponse.questions.map { toDomain(it) },
            durationMinutes = startExamResponse.durationMinutes,
            startTime = System.currentTimeMillis()
        )
    }
}
