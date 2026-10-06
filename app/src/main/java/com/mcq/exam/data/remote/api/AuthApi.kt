package com.mcq.exam.data.remote.api

import com.mcq.exam.data.remote.dto.AuthResponseDto
import com.mcq.exam.data.remote.dto.LoginRequest
import com.mcq.exam.data.remote.dto.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponseDto>

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponseDto>
}

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class AuthResponseDto(
    val user: UserDto,
    val token: String
)

data class UserDto(
    val id: String,
    val name: String,
    val email: String
)
