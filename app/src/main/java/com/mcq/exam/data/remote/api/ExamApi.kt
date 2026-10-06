package com.mcq.exam.data.remote.api

import com.mcq.exam.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ExamApi {
    @GET("exams")
    suspend fun getExams(
        @Query("subject_id") subjectId: String? = null
    ): Response<List<ExamDto>>

    @GET("exams/{id}")
    suspend fun getExamById(@Path("id") examId: String): Response<ExamDto>

    @POST("exams/{id}/start")
    suspend fun startExam(@Path("id") examId: String): Response<StartExamResponse>

    @POST("attempts/{id}/submit")
    suspend fun submitExam(
        @Path("id") attemptId: String,
        @Body request: SubmitExamRequest
    ): Response<SubmitExamResponse>

    @GET("attempts/{id}/review")
    suspend fun getAttemptReview(@Path("id") attemptId: String): Response<ReviewResponse>

    @GET("attempts")
    suspend fun getUserAttempts(
        @Query("exam_id") examId: String? = null
    ): Response<List<AttemptDto>>
}
