package com.mcq.exam.data.repository

import com.mcq.exam.data.remote.api.ExamApi
import com.mcq.exam.data.remote.mapper.DtoMapper
import com.mcq.exam.domain.model.Attempt
import com.mcq.exam.domain.repository.IAttemptRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AttemptRepository @Inject constructor(
    private val examApi: ExamApi
) : IAttemptRepository {

    override suspend fun getUserAttempts(examId: String?): Result<List<Attempt>> {
        return try {
            val response = examApi.getUserAttempts(examId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.map { DtoMapper.toDomain(it) })
            } else {
                Result.failure(Exception("Failed to fetch attempts: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
