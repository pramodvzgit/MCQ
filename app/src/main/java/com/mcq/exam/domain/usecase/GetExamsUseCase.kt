package com.mcq.exam.domain.usecase

import com.mcq.exam.domain.model.Exam
import com.mcq.exam.domain.repository.IExamRepository
import javax.inject.Inject

class GetExamsUseCase @Inject constructor(
    private val examRepository: IExamRepository
) {
    suspend operator fun invoke(subjectId: String? = null): Result<List<Exam>> {
        return examRepository.getExams(subjectId)
    }
}
