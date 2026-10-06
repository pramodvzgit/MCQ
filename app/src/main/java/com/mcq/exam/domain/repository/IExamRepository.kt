package com.mcq.exam.domain.repository

import com.mcq.exam.domain.model.Exam

interface IExamRepository {
    suspend fun getExams(subjectId: String? = null): Result<List<Exam>>
    suspend fun getExamById(examId: String): Result<Exam?>
}
