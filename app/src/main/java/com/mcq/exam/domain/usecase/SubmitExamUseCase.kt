package com.mcq.exam.domain.usecase

import com.mcq.exam.domain.repository.IQuestionRepository
import javax.inject.Inject

class SubmitExamUseCase @Inject constructor(
    private val questionRepository: IQuestionRepository
) {
    suspend operator fun invoke(
        attemptId: String,
        answers: Map<String, String>
    ): Result<IQuestionRepository.SubmitResult> {
        return questionRepository.submitExam(attemptId, answers)
    }
}
