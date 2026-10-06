package com.mcq.exam.domain.usecase

import com.mcq.exam.domain.model.Attempt
import com.mcq.exam.domain.repository.IAttemptRepository
import javax.inject.Inject

class GetAttemptHistoryUseCase @Inject constructor(
    private val attemptRepository: IAttemptRepository
) {
    suspend operator fun invoke(examId: String? = null): Result<List<Attempt>> {
        return attemptRepository.getUserAttempts(examId)
    }
}
