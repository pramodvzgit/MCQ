package com.mcq.exam.domain.repository

import com.mcq.exam.domain.model.AuthResponse
import com.mcq.exam.domain.model.User

interface IAuthRepository {
    suspend fun register(name: String, email: String, password: String): Result<AuthResponse>
    suspend fun login(email: String, password: String): Result<AuthResponse>
    suspend fun getCurrentUser(): Result<User?>
    suspend fun logout(): Result<Unit>
}
