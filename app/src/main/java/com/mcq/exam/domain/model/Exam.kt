package com.mcq.exam.domain.model

data class Exam(
    val id: String,
    val title: String,
    val description: String?,
    val subject: Subject,
    val durationMinutes: Int,
    val totalQuestions: Int,
    val passingPercentage: Double
)

data class Subject(
    val id: String,
    val name: String
)
