package com.mcq.exam.domain.usecase

import com.mcq.exam.domain.model.ExamSession
import com.mcq.exam.domain.repository.IQuestionRepository
import javax.inject.Inject

class StartExamUseCase @Inject constructor(
    private val questionRepository: IQuestionRepository
) {
    suspend operator fun invoke(examId: String): Result<ExamSession> {
        return questionRepository.startExam(examId)
    }
}
