package com.mcq.exam.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ExamDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("subject")
    val subject: SubjectDto,
    @SerializedName("duration_minutes")
    val durationMinutes: Int,
    @SerializedName("total_questions")
    val totalQuestions: Int,
    @SerializedName("passing_percentage")
    val passingPercentage: Double
)

data class SubjectDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)
