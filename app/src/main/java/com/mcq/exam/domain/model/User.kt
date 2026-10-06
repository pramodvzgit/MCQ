package com.mcq.exam.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String
)

data class AuthResponse(
    val user: User,
    val token: String
)
