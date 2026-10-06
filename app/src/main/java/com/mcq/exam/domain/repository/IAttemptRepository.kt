package com.mcq.exam.domain.repository

import com.mcq.exam.domain.model.Attempt

interface IAttemptRepository {
    suspend fun getUserAttempts(examId: String? = null): Result<List<Attempt>>
}
