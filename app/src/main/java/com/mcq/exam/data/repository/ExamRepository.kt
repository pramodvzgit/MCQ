package com.mcq.exam.data.repository

import com.mcq.exam.data.remote.api.ExamApi
import com.mcq.exam.data.remote.mapper.DtoMapper
import com.mcq.exam.domain.model.Exam
import com.mcq.exam.domain.repository.IExamRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExamRepository @Inject constructor(
    private val examApi: ExamApi
) : IExamRepository {

    override suspend fun getExams(subjectId: String?): Result<List<Exam>> {
        return try {
            val response = examApi.getExams(subjectId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.map { DtoMapper.toDomain(it) })
            } else {
                Result.failure(Exception("Failed to fetch exams: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getExamById(examId: String): Result<Exam?> {
        return try {
            val response = examApi.getExamById(examId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(DtoMapper.toDomain(response.body()!!))
            } else if (response.code() == 404) {
                Result.success(null)
            } else {
                Result.failure(Exception("Failed to fetch exam: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
